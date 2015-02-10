package org.example.components;

import org.example.beans.Simpledocument;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleComponent extends BaseHstComponent {

    public static final Logger log = LoggerFactory.getLogger(SimpleComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        final HstRequestContext ctx = request.getRequestContext();

        // Load the document based on the URL
        Simpledocument document = (Simpledocument) ctx.getContentBean();

        if (document != null) {
            // Pass the loaded document to the request
            request.setAttribute("document", document);
        }
    }
}