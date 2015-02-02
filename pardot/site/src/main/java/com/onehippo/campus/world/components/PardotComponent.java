package com.onehippo.campus.world.components;

import com.onehippo.campus.world.targeting.PardotData;
import com.onehippo.cms7.targeting.ProfileProvider;
import com.onehippo.cms7.targeting.TargetingProfile;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.pagecomposer.builtin.components.StandardContainerComponent;

public class PardotComponent extends StandardContainerComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response)
            throws HstComponentException {
        super.doBeforeRender(request, response);

        TargetingProfile profile = ProfileProvider.get();
        if (profile != null) {
            PardotData data = profile.getTargetingData("pardot");
            if (data != null && data.getLists().contains("GetTogether2014 Participants")) {
                request.setAttribute("pardot", data);
            }
        }
    }

}
