package org.example.frontend;

import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.example.frontend.dialog.CommentEditingDialog;
import org.example.workflow.CommentEditingWorkflow;
import org.hippoecm.addon.workflow.StdWorkflow;
import org.hippoecm.frontend.dialog.IDialogService;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.plugins.reviewedactions.AbstractDocumentWorkflowPlugin;
import org.hippoecm.repository.api.Workflow;

public class CommentEditingFrontendPlugin extends AbstractDocumentWorkflowPlugin {

    private static final long serialVersionUID = 1L;


    public CommentEditingFrontendPlugin(final IPluginContext context, final IPluginConfig config) {
        super(context, config);

        add(new StdWorkflow(
                "action",
                new StringResourceModel("my-button-label", this, null, new Object[0]),
                context,
                getModel()) {

            public String comment;

            @Override
            protected IDialogService.Dialog createRequestDialog() {
                return new CommentEditingDialog(this);
            }

            @Override
            protected String execute(Workflow wf) throws Exception {
                CommentEditingWorkflow workflow = (CommentEditingWorkflow) wf;
                workflow.comment(comment);
                return null;
            }

            public String getSubMenu() {
                return "top";
            }

            protected ResourceReference getIcon() {
                return new PackageResourceReference(this.getClass(), "my-icon.png");
            }
        });
    }
}