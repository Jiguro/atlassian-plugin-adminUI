package com.atlassian.plugins.tutorial.refapp.jira.workflow;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginFunctionFactory;
import com.atlassian.jira.workflow.WorkflowManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

@Named("MyPostFunctionFactory")
public class MyPostFunctionFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginFunctionFactory {
    public static final String FIELD_MESSAGE = "messageField";

    public static final String USERS = "users";

    public static final String USER = "user";

    @ComponentImport
    private final WorkflowManager workflowManager;

    @Inject
    public MyPostFunctionFactory(final WorkflowManager workflowManager) {
        this.workflowManager = workflowManager;
    }

    @Override
    protected void getVelocityParamsForInput(final Map<String, Object> velocityParams) {
        //   Map<String, String[]> myParams = ActionContext.getParameters();
        //   final JiraWorkflow jiraWorkflow = workflowManager.getWorkflow(myParams.get("workflowName")[0]);

        //the default message
        // velocityParams.put(FIELD_MESSAGE, "Workflow Last Edited By " + jiraWorkflow.getUpdateAuthorName());

        velocityParams.put(USERS, ComponentAccessor.getUserManager().getAllApplicationUsers());

    }

    @Override
    protected void getVelocityParamsForEdit(final Map<String, Object> velocityParams, final AbstractDescriptor descriptor) {
        //   getVelocityParamsForInput(velocityParams);
        //    getVelocityParamsForView(velocityParams, descriptor);

        velocityParams.put(USERS, ComponentAccessor.getUserManager().getAllApplicationUsers());
    }

    @Override
    protected void getVelocityParamsForView(final Map<String, Object> velocityParams, final AbstractDescriptor descriptor) {
        if (!(descriptor instanceof FunctionDescriptor)) {
            throw new IllegalArgumentException("Descriptor must be a FunctionDescriptor.");
        }

        final FunctionDescriptor functionDescriptor = (FunctionDescriptor) descriptor;

        // String message = (String)functionDescriptor.getArgs().get(FIELD_MESSAGE);

        String user = (String) functionDescriptor.getArgs().get(USER);

        if (user == null) {
            user = "No User";
        }

        velocityParams.put(USER, user);
    }


    @Override
    public Map<String, ?> getDescriptorParams(final Map<String, Object> formParams) {
        final Map params = new HashMap();

        // Process The map
        final String message = extractSingleParam(formParams, USER);
        params.put(USER, message);

        return params;
    }

}