package imageutil;

public class CloudinaryScalingParameters  {

    private int width;
    private int height;
    private String format;
    private String crop;
    private String gravity;
    private String effect;
    private boolean upscaling;

    public int getWidth() {
        return width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(final String format) {
        this.format = format;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(final String crop) {
        this.crop = crop;
    }

    public String getGravity() {
        return gravity;
    }

    public void setGravity(final String gravity) {
        this.gravity = gravity;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(final String effect) {
        this.effect = effect;
    }

    public boolean getUpscaling() {
        return upscaling;
    }

    public void setUpscaling(final boolean upscaling) {
        this.upscaling = upscaling;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CloudinaryScalingParameters)) {
            return false;
        }

        final CloudinaryScalingParameters that = (CloudinaryScalingParameters) o;

        if (height != that.height) {
            return false;
        }
        if (upscaling != that.upscaling) {
            return false;
        }
        if (width != that.width) {
            return false;
        }
        if (crop != null ? !crop.equals(that.crop) : that.crop != null) {
            return false;
        }
        if (effect != null ? !effect.equals(that.effect) : that.effect != null) {
            return false;
        }
        if (format != null ? !format.equals(that.format) : that.format != null) {
            return false;
        }
        return !(gravity != null ? !gravity.equals(that.gravity) : that.gravity != null);

    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        result = 31 * result + (format != null ? format.hashCode() : 0);
        result = 31 * result + (crop != null ? crop.hashCode() : 0);
        result = 31 * result + (gravity != null ? gravity.hashCode() : 0);
        result = 31 * result + (effect != null ? effect.hashCode() : 0);
        result = 31 * result + (upscaling ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CloudinaryScalingParameters{" +
                "width=" + width +
                ", height=" + height +
                ", format='" + format + '\'' +
                ", crop='" + crop + '\'' +
                ", gravity='" + gravity + '\'' +
                ", effect='" + effect + '\'' +
                ", upscaling=" + upscaling +
                '}';
    }
}
