package org.example.frontend.dialog;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.IValueMap;
import org.hippoecm.addon.workflow.AbstractWorkflowDialog;
import org.hippoecm.addon.workflow.StdWorkflow;
import org.hippoecm.frontend.dialog.DialogConstants;

public class CommentEditingDialog extends AbstractWorkflowDialog<Void> {

    public CommentEditingDialog(StdWorkflow action) {
        super(null, action);
        TextArea<String> commentArea = new TextArea<>(
                "commentArea",
                new PropertyModel<String>(action, "comment"));
        commentArea.setOutputMarkupId(true);
        add(commentArea);
    }

    @Override
    public IModel<String> getTitle() {
        return new StringResourceModel("my-dialog-title", this, null, new Object[0]);
    }

    @Override
    public IValueMap getProperties() {
        return DialogConstants.SMALL;
    }
}
