package imageutil;

import java.awt.Dimension;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import com.cloudinary.Cloudinary;

import org.apache.jackrabbit.JcrConstants;
import org.hippoecm.frontend.editor.plugins.resource.ResourceHelper;
import org.hippoecm.frontend.plugins.gallery.model.GalleryException;
import org.hippoecm.frontend.plugins.gallery.processor.AbstractGalleryProcessor;
import org.hippoecm.repository.gallery.HippoGalleryNodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CloudinaryGalleryProcessor extends AbstractGalleryProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudinaryGalleryProcessor.class);

    /**
     * Map of JCR node names to scaling parameters.
     */
    protected final Map<String, CloudinaryScalingParameters> cloudinaryParametersMap;
    private Cloudinary cloudinary;

    public CloudinaryGalleryProcessor() {
        this.cloudinaryParametersMap = new HashMap<String, CloudinaryScalingParameters>();
    }

    /**
     * Adds the scaling parameters for a resource node. Existing scaling parameters for this node are replaced and
     * returned.
     *
     * @param nodeName the name of the resource node.
     * @param parameters the scaling parameters to use when creating a scaled version of the image in the resource node
     *
     * @return the previous scaling parameters of the resource node with the given name, or <code>null</code> if no
     * previous scaling parameters existed.
     */
    public CloudinaryScalingParameters addScalingParameters(String nodeName, CloudinaryScalingParameters parameters) {
        return cloudinaryParametersMap.put(nodeName, parameters);
    }

    /**
     * Stores the given file name in the given node.
     */
    @Override
    public void initGalleryNode(Node node, InputStream data, String mimeType, String fileName) throws RepositoryException {
        node.setProperty(HippoGalleryNodeType.IMAGE_SET_FILE_NAME, fileName);
    }


    /**
     * Initializes a hippo:resource node of an the main gallery node. Such initialization happens at two times: when a new image is uploaded to the
     * gallery, and when an image in an existing imageset is replaced by another image.
     *
     * @param node         the hippo:resource node
     * @param data         the uploaded data
     * @param mimeType     the MIME type of the uploaded data
     * @param fileName     the file name of the uploaded data
     * @param lastModified
     * @throws javax.jcr.RepositoryException when repository access failed.
     */
    @Override
    public void initGalleryResource(final Node node, final InputStream data, final String mimeType, final String fileName, final Calendar lastModified) throws RepositoryException {
        node.setProperty(JcrConstants.JCR_MIMETYPE, mimeType);
        node.setProperty(JcrConstants.JCR_LASTMODIFIED, lastModified);

        InputStream stored = data;
        int width = 0;
        int height = 0;
        final String nodeName = node.getName();
        final CloudinaryScalingParameters p = cloudinaryParametersMap.get(nodeName);

        if (isImageMimeType(mimeType)) {
                try {
                    final CloudinaryImageOperation cloudinaryImageOperation = new CloudinaryImageOperation();
                    cloudinaryImageOperation.setCloudinary(cloudinary);
                    cloudinaryImageOperation.setWidth(p.getWidth());
                    cloudinaryImageOperation.setHeight(p.getHeight());
                    cloudinaryImageOperation.setGravity(p.getGravity());
                    cloudinaryImageOperation.setCrop(p.getCrop());
                    cloudinaryImageOperation.setEffect(p.getEffect());
                    cloudinaryImageOperation.setFormat(p.getFormat());
                    cloudinaryImageOperation.execute(data, mimeType);
                    stored = cloudinaryImageOperation.getData();
                    width = cloudinaryImageOperation.getScaledWidth();
                    height = cloudinaryImageOperation.getScaledHeight();
                } catch (GalleryException e) {
                    LOGGER.warn("Scaling and cropping  failed, using original image instead", e);
                }
        } else {
            LOGGER.debug("Unknown image MIME type: {}, using raw data", mimeType);
        }

        node.setProperty(JcrConstants.JCR_DATA, ResourceHelper.getValueFactory(node).createBinary(stored));
        node.setProperty(HippoGalleryNodeType.IMAGE_WIDTH, (long) width);
        node.setProperty(HippoGalleryNodeType.IMAGE_HEIGHT, (long) height);
    }



    public Dimension getDesiredResourceDimension(Node resource) throws GalleryException, RepositoryException {
        String nodeName = resource.getName();
        CloudinaryScalingParameters scaleOperation = cloudinaryParametersMap.get(nodeName);
        if (scaleOperation != null) {
            int width = scaleOperation.getWidth();
            int height = scaleOperation.getHeight();
            return new Dimension(width, height);
        } else {
            return null;
        }
    }

    public boolean isUpscalingEnabled(Node resource) throws GalleryException, RepositoryException {
        String nodeName = resource.getName();
        CloudinaryScalingParameters scaleOperation = cloudinaryParametersMap.get(nodeName);
        return scaleOperation == null || scaleOperation.getUpscaling();
    }


    public void setCloudinary(final Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Cloudinary getCloudinary() {
        return cloudinary;
    }
}
