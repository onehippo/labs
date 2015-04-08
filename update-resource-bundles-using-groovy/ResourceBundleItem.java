package nl.company.cms.fundamentals.cms.utils;

import org.apache.commons.lang.StringUtils;

/**
 * Represents a resourcebundle message instruction (ADD or DELETE), which consists of a key, message (default/NL) and a description.
 *
 * <p>
 *     This bean is used by the <code>ResourceBundleBootstrapUtilities</code> class to load instructions from the property file into
 *     a list of ResourceBundleItem objects. The list is read by the Groovy script.
 * </p>
 */
public class ResourceBundleItem {

    public enum ItemAction { ADD, DELETE; }


    private String key;
    private ItemAction action;
    private String message;
    private String messageNl;
    private String description;

    public ResourceBundleItem(String key, ItemAction action){
        this.key = key;
        this.action = action;

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ItemAction getAction() {
        return action;
    }

    public void setAction(ItemAction action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageNl() {
        return messageNl;
    }

    public void setMessageNl(String messageNl) {
        this.messageNl = messageNl;
    }

    public String getDescription() {
        if(StringUtils.isEmpty(description)){
            return StringUtils.EMPTY;
        } else {
            return description;
        }
    }

    /**
     * Valid means: at least a key, message and message_nl field set (not blank). The description isn't mandatory.
     * @return
     */
    public boolean isValid() {
        if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(message) && StringUtils.isNotBlank(messageNl)) {
            return true;
        }

        return false;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
