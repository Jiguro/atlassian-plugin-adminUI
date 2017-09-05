package com.atlassian.plugins.tutorial.refapp.jira.workflow;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.workflow.function.issue.AbstractJiraFunctionProvider;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.inject.Named;
import java.util.Map;

@Named("MyPostFunction")
public class MyPostFunction extends AbstractJiraFunctionProvider {

    private final Client restClient;

    public MyPostFunction() {
        restClient = Client.create();
    }

    @Override
    public void execute(final Map transientVars, final Map args, final PropertySet ps) throws WorkflowException {
        invokeRequest(getIssue(transientVars), ComponentAccessor.getUserManager().getUser(args.get("user").toString()));
    }


    private void invokeRequest(final MutableIssue issue, final ApplicationUser user) {
        final WebResource webResource = restClient.resource("http://requestb.in/x389jqx3?inspect").queryParam("user", user.getDisplayName());
        final ClientResponse response = webResource.get(ClientResponse.class);

        final CommentManager commentManager = ComponentAccessor.getCommentManager();
        commentManager.create(
                issue,
                user,
                String.format("GET request to %s has result %s", webResource.getURI(), response.getStatus()),
                false);
    }

}