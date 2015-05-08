package org.example.workflow;

public interface CommentEditingWorkflow extends org.hippoecm.repository.api.Workflow {
    public void comment(String comment) throws javax.jcr.RepositoryException;
}
