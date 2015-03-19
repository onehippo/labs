package imageutil.plugin;

import com.cloudinary.Cloudinary;

import org.apache.cxf.common.util.StringUtils;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.Plugin;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("WeakerAccess")
public class CloudinaryService extends Plugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudinaryService.class);
    public static final String CLOUD_NAME = "cloud_name";
    public static final String API_KEY = "api_key";
    public static final String API_SECRET = "api_secret";

    private Cloudinary cloudinary;

    public CloudinaryService(final IPluginContext context, final IPluginConfig config) {
        super(context, config);
        context.registerService(this, config.getString("cloudinary.service.id", CloudinaryService.class.getName()));
        init();
    }

    public Cloudinary getCloudinary() {
        return cloudinary;
    }

    private void init() {
        String cloudName = getPluginConfig().getString(CLOUD_NAME);
        String apiKey = getPluginConfig().getString(API_KEY);
        String apiSecret = getPluginConfig().getString(API_SECRET);

        if (StringUtils.isEmpty(cloudName)||StringUtils.isEmpty(apiKey)|| StringUtils.isEmpty(apiSecret)){
            LOGGER.warn("Cloudinary initialization parameters has not been set, "
                    + "Cloudinary can not be used. "
                    + "Please set the parameters cloud_name, api_key and api_secret");
        }

        cloudinary = new Cloudinary(Cloudinary.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }
}
