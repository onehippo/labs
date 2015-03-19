package imageutil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.common.util.StringUtils;
import org.hippoecm.frontend.plugins.gallery.imageutil.AbstractImageOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CloudinaryImageOperation extends AbstractImageOperation {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudinaryImageOperation.class);
    private InputStream scaledData;
    private int scaledWidth;
    private int width;
    private int height;
    private String gravity;
    private String crop;
    private String effect;
    private String format;
    private int scaledHeight;
    private Cloudinary cloudinary;

    public CloudinaryImageOperation() {
    }

    public void execute(final InputStream data, final ImageReader reader, final ImageWriter writer) throws IOException {
        // save the image data in a temporary file so we can reuse the original data as-is if needed without
        // putting all the data into memory
        if (cloudinary == null) {
            return;
        }
        final File tmpFile = writeToTmpFile(data);
        LOGGER.debug("Stored uploaded image in temporary file {}", tmpFile);

        InputStream dataInputStream = null;
        ImageInputStream imageInputStream = null;
        try {

            dataInputStream = new FileInputStream(tmpFile);
            imageInputStream = new MemoryCacheImageInputStream(dataInputStream);
            reader.setInput(imageInputStream);

            Map upload;
            int width = getWidth();
            if (width == 0) {
                width = reader.getWidth(0);
            }
            int height = getHeight();
            if (height == 0) {
                height = reader.getHeight(0);
            }

            Transformation transformation = new Transformation();
            transformation = transformation.width(width).height(height);
            String crop = getCrop();
            if (!StringUtils.isEmpty(crop)) {
                transformation = transformation.crop(crop);
            }
            String effect = getEffect();
            if (!StringUtils.isEmpty(effect)) {
                transformation = transformation.effect(effect);
            }
            if (!StringUtils.isEmpty(gravity)) {
                transformation = transformation.gravity(gravity);
            }

            upload = cloudinary.uploader().upload(tmpFile, Cloudinary.asMap(
                    "transformation", transformation));

            String format = getFormat();
            if (StringUtils.isEmpty(format)) {
                format = (String) upload.get("format");
            }

            String url = cloudinary.url().format(format).generate((String) upload.get("public_id"));
            scaledData = new URL(url).openStream();
            scaledWidth = width;
            scaledHeight = height;

        } finally {
            if (imageInputStream != null) {
                imageInputStream.close();
            }
            IOUtils.closeQuietly(dataInputStream);
            LOGGER.debug("Deleting temporary file {}", tmpFile);
            boolean delete = tmpFile.delete();
            if (!delete){
                LOGGER.warn("{} could not be deleted, please delete manually",tmpFile.getAbsolutePath());
            }
        }
    }


    public InputStream getData() {
        return scaledData;
    }

    public int getScaledHeight() {
        return scaledHeight;
    }

    public int getScaledWidth() {
        return scaledWidth;
    }

    private File writeToTmpFile(InputStream data) throws IOException {
        File tmpFile = File.createTempFile("hippo-image", ".tmp");
        OutputStream tmpStream = null;
        try {
            tmpStream = new BufferedOutputStream(new FileOutputStream(tmpFile));
            IOUtils.copy(data, tmpStream);
        } finally {
            IOUtils.closeQuietly(tmpStream);
        }
        return tmpFile;
    }


    public void setWidth(final int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setGravity(final String gravity) {
        this.gravity = gravity;
    }

    public String getGravity() {
        return gravity;
    }

    public void setCrop(final String crop) {
        this.crop = crop;
    }

    public String getCrop() {
        return crop;
    }

    public void setEffect(final String effect) {
        this.effect = effect;
    }

    public String getEffect() {
        return effect;
    }

    public void setFormat(final String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setCloudinary(final Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Cloudinary getCloudinary() {
        return cloudinary;
    }
}
