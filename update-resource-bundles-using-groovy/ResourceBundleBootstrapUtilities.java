package nl.company.cms.fundamentals.cms.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static nl.company.cms.fundamentals.cms.utils.ResourceBundleItem.ItemAction;

/**
 * Utility class used by the Groovy script 'auto-add-delete-resourcemesages' to find property files
 * on the classpath (in any cms- plugin or directly in the cms war). The util returns a list
 * with <code>ResourceItem</code> objects. The list is being processed by groovy and updates
 * the resourcebundle:resourcebundle nodes.
 *
 * <p><strong>Property files</strong>
 * All property files must be created in -bootstrap or -cms part of the cms plugin JAR (that's because the Groovy script
 * runs on the CMS classpath, so property files stored in the site part aren't visible/reachable).
 * Within in the cms JAR, property files must be located in a 'labels' package, created in the resource section. Property files can have any name (*.properties).</p>
 *
 * <p>
 * Correct location: cms-mijnprofiel/cms-mijnprofiel-bootstrap/src/main/resources/labels/example-add.properties<br/>
 * Incorrect location: cms-mijnprofiel/cms-mijnprofiel-site/src/main/resources/labels/example-add.properties
 * </p>
 *
 * In the property file, please use the following format:
 * <pre>
 * #the ID of the resourcebundle
 * bundle.id=nl.company.cms.plugins.MyProfileBundle
 * #possible values: ADD | DELETE
 * bundle.action=ADD
 *
 * #every message has three rows: you can have multiple messages
 * baanvoorkeuren.baanvoorkeuren.saved.message=your job preferences where successfully updated
 * baanvoorkeuren.baanvoorkeuren.saved.message_nl=Je baanvoorkeuren zijn opgeslagen.
 * baanvoorkeuren.baanvoorkeuren.saved.description=Dit is een omschrijving
 *
 * baanvoorkeuren.baanvoorkeuren.load.message=your job prefs has been loaded
 * baanvoorkeuren.baanvoorkeuren.load.message_nl=je baanvoorkeuren zijn geladen.
 * baanvoorkeuren.baanvoorkeuren.load.description=Weer een andere omschrijving
 * </pre>
 *
 * As you can see, every message is constructured like<br/>
 * <strong>key</strong> dot <strong>message | message_nl | description</strong> = text string
 */
public class ResourceBundleBootstrapUtilities {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceBundleBootstrapUtilities.class);

    public static final String PROPERTY_ID = "bundle.id";
    public static final String PROPERTY_ACTION = "bundle.action";
    public static final String PROPERTY_MESSAGE = "message";
    public static final String PROPERTY_MESSAGENL = "message_nl";
    public static final String PROPERTY_DESCRIPTION = "description";

    public static List<ResourceBundleItem> findResourceBundleConfig(String bundleId) {
        final List<ResourceBundleItem> bundleMap = new ArrayList<>();

        try {

            Set<URL> urlResources = findLabelResources();
            for(URL url : urlResources){
                InputStream in = url.openStream();
                Properties prop = new Properties();
                prop.load(in);
                if(prop.containsKey("bundle.id") && prop.getProperty("bundle.id").equalsIgnoreCase(bundleId)){

                    List<ResourceBundleItem> bundleItemList = processProperties(url, prop);
                    bundleMap.addAll(bundleItemList);
                }
                in.close();
            }

        } catch (Exception e) {
            LOG.error("Error loading property file: " + e.getLocalizedMessage(), e);
        }

        return bundleMap;
    }

    /**
     * Process every property file (converted as Property object).
     *
     */
    private static List<ResourceBundleItem> processProperties(URL url, Properties prop) {

        Map<String, ResourceBundleItem> bundleMap = new HashMap<>();
        try {

            ItemAction action = ItemAction.valueOf(prop.getProperty("bundle.action"));
            for (String propKey : prop.stringPropertyNames()) {
                if (propKey.equalsIgnoreCase(PROPERTY_ID) == false && propKey.equalsIgnoreCase(PROPERTY_ACTION) == false) {

                    String key = propKey.substring(0, propKey.lastIndexOf("."));
                    ResourceBundleItem item;
                    if (bundleMap.containsKey(key)) {
                        item = bundleMap.get(key);
                    } else {
                        item = new ResourceBundleItem(key, action);
                    }
                    String subKey = propKey.substring(propKey.lastIndexOf(".") + 1);
                    switch (subKey) {
                        case PROPERTY_MESSAGE:
                            item.setMessage(prop.getProperty(propKey));
                            break;
                        case PROPERTY_MESSAGENL:
                            item.setMessageNl(prop.getProperty(propKey));
                            break;
                        case PROPERTY_DESCRIPTION:
                            item.setDescription(prop.getProperty(propKey));
                            break;
                    }
                    bundleMap.put(key, item);
                }
            }
        } catch(Exception e) {
            LOG.error("An error occured while reading resourcebundle property file '" + url.getFile()  + "'", e);
        }

        //after we finished building the model, we check whether the model is valid or not
        Iterator<Map.Entry<String, ResourceBundleItem>> it = bundleMap.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, ResourceBundleItem> entry = it.next();
            if(entry.getValue().isValid() == false) {
                it.remove();
            }
        }

        return new ArrayList<>(bundleMap.values());
    }

    /**
     * Find all available property files on the classpath, returned as URL objects.
     *
     */
    private static Set<URL> findLabelResources() throws IOException{

        Set<URL> result = new HashSet<>();

        String rootDirPath = "classpath*:/labels/";
        String relativePath = rootDirPath.substring("classpath*:".length());
        if (relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1);
        }
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration resourceUrls = cl.getResources(relativePath);
        Set<URL> rootDirResources = new HashSet<>();
        while (resourceUrls.hasMoreElements()) {
            URL url = (URL) resourceUrls.nextElement();
            rootDirResources.add(url);
        }

        for (URL url : rootDirResources) {

            URLConnection con = url.openConnection();
            /*
             * propery files in JARs (all Hippo cms plugins)
             */
            if (con instanceof JarURLConnection) {

                JarURLConnection jarCon = (JarURLConnection) con;
                jarCon.setUseCaches(false);
                JarFile jarFile = jarCon.getJarFile();
                JarEntry jarEntry = jarCon.getJarEntry();
                String rootEntryPath = (jarEntry != null ? jarEntry.getName() : "");

                if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/")) {
                    // Root entry path must end with slash to allow for proper matching.
                    // The Sun JRE does not return a slash here, but BEA JRockit does.
                    rootEntryPath = rootEntryPath + "/";
                }

                for (Enumeration entries = jarFile.entries(); entries.hasMoreElements(); ) {
                    JarEntry entry = (JarEntry) entries.nextElement();
                    String entryPath = entry.getName();
                    if (entryPath.startsWith(rootEntryPath)) {
                        String relativePath2 = entryPath.substring(rootEntryPath.length());
                        if (relativePath2.endsWith(".properties")) {
                            result.add(new URL(url, relativePath2));
                        }
                    }
                }

            /*
             * property files directly stored in the WAR (located in /WEB-INF/classes/labels)
             */
            } else if (con instanceof FileURLConnection) {

                FileURLConnection fileCon = (FileURLConnection) con;
                File dir = new File(fileCon.getURL().getFile());
                File[] files = dir.listFiles();

                for(File file : files) {
                    if(file.getPath().endsWith(".properties")) {
                        result.add(file.toURI().toURL());
                    }
                }
            }
        }


        return result;
    }
}
