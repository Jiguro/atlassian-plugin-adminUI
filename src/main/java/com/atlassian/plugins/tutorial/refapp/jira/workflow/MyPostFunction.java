package com.atlassian.plugins.tutorial.refapp.jira.workflow;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.watchers.WatcherManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserUtil;
import com.atlassian.jira.workflow.function.issue.AbstractJiraFunctionProvider;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

@Named("MyPostFunction")
public class MyPostFunction extends AbstractJiraFunctionProvider {

    @ComponentImport
    private final CustomFieldManager customFieldManager;
    @ComponentImport
    private final JiraAuthenticationContext authContext;
    @ComponentImport
    private final UserUtil userUtil;
    @ComponentImport
    private final ProjectRoleManager projectRoleManager;

    @Inject
    public MyPostFunction(final CustomFieldManager customFieldManager,
                          final JiraAuthenticationContext authContext, final UserUtil userUtil,
                          final ProjectRoleManager projectRoleManager) {
        this.customFieldManager = customFieldManager;
        this.authContext = authContext;
        this.userUtil = userUtil;
        this.projectRoleManager = projectRoleManager;
    }

    @Override
    public void execute(final Map transientVars, final Map args, final PropertySet ps) throws WorkflowException {
        addWatcher(getIssue(transientVars), ComponentAccessor.getUserManager().getUser(args.get("user").toString()));
    }


    private void addWatcher(final MutableIssue issue, final ApplicationUser user) {
        final WatcherManager watcherManager = ComponentAccessor.getWatcherManager();
        watcherManager.startWatching(user, issue);
    }

}