package com.onehippo.campus.world.targeting;

import com.onehippo.cms7.targeting.frontend.plugin.CharacteristicPlugin;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.wicketstuff.js.ext.util.ExtClass;

@ExtClass("Hippo.Campus.PardotCharacteristicPlugin")
public class PardotCharacteristicPlugin extends CharacteristicPlugin {

    public PardotCharacteristicPlugin(IPluginContext context, IPluginConfig config) {
        super(context, config);
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "PardotCharacteristicPlugin.js")));
    }
}

