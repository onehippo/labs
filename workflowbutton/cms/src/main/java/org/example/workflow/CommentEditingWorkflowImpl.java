package org.example.workflow;

import java.rmi.RemoteException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.hippoecm.repository.ext.WorkflowImpl;

public class CommentEditingWorkflowImpl extends WorkflowImpl implements CommentEditingWorkflow {
    private static final long serialVersionUID = 1L;

    public CommentEditingWorkflowImpl() throws RemoteException {}

    @Override
    public void comment(final String comment) throws RepositoryException {
        Node documentHandleNode = getCheckedOutNode();
        if(! documentHandleNode.isNodeType("hippostd:relaxed")) {
            documentHandleNode.addMixin("hippostd:relaxed");
        }
        documentHandleNode.setProperty("comment", comment);
        documentHandleNode.getSession().save();
    }
}
