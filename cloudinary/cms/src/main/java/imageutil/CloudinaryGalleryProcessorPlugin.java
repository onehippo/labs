package imageutil;

import com.cloudinary.Cloudinary;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.Plugin;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.plugins.gallery.model.GalleryProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import imageutil.plugin.CloudinaryService;


public class CloudinaryGalleryProcessorPlugin extends Plugin {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryGalleryProcessorPlugin.class);

    protected static final String CONFIG_PARAM_WIDTH = "width";
    protected static final String CONFIG_PARAM_HEIGHT = "height";
    protected static final String CONFIG_PARAM_CROP = "crop";
    protected static final String CONFIG_PARAM_EFFECT = "effect";
    protected static final String CONFIG_PARAM_GRAVITY = "gravity";

    protected static final int DEFAULT_WIDTH = 0;
    protected static final int DEFAULT_HEIGHT = 0;


    @SuppressWarnings("WeakerAccess")
    public CloudinaryGalleryProcessorPlugin(final IPluginContext context, final IPluginConfig config) {
        super(context, config);
        final GalleryProcessor processor = createCloudinaryGalleryProcessor(config);
        final String id = config.getString("gallery.processor.id", "service.cloudinary.gallery.processor");
        context.registerService(processor, id);
    }

    protected CloudinaryGalleryProcessor createCloudinaryGalleryProcessor(IPluginConfig config) {
        final CloudinaryGalleryProcessor processor = new CloudinaryGalleryProcessor();

        CloudinaryService service = getPluginContext().getService(getPluginConfig().getString("cloudinary.service.id",
                CloudinaryService.class.getName()), CloudinaryService.class);
        Cloudinary cloudinary = service.getCloudinary();

        for (IPluginConfig cloudinaryConfig: config.getPluginConfigSet()) {
            final String nodeName = StringUtils.substringAfterLast(cloudinaryConfig.getName(), ".");

            if (!StringUtils.isEmpty(nodeName)) {
                int width = cloudinaryConfig.getAsInteger(CONFIG_PARAM_WIDTH, DEFAULT_WIDTH);
                int height = cloudinaryConfig.getAsInteger(CONFIG_PARAM_HEIGHT, DEFAULT_HEIGHT);
                final String crop = cloudinaryConfig.getString(CONFIG_PARAM_CROP, "scale");
                final String gravity = cloudinaryConfig.getString(CONFIG_PARAM_GRAVITY, "");
                final String effect = cloudinaryConfig.getString(CONFIG_PARAM_EFFECT,"");

                final CloudinaryScalingParameters parameters = new CloudinaryScalingParameters();
                parameters.setCrop(crop);
                parameters.setEffect(effect);
                parameters.setGravity(gravity);
                parameters.setWidth(width);
                parameters.setHeight(height);
                log.debug("Scaling parameters for {}: {}", nodeName, parameters);
                processor.addScalingParameters(nodeName, parameters);
                processor.setCloudinary(cloudinary);
            }
        }
        return processor;
    }
}

