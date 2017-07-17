package com.atlassian.plugins.tutorial.refapp.impl;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.plugins.tutorial.refapp.api.MyPluginComponent;

import javax.inject.Inject;
import javax.inject.Named;

@ExportAsService(MyPluginComponent.class)
@Named ("myPluginComponent")
public class MyPluginComponentImpl implements MyPluginComponent {

    @ComponentImport
    private final ApplicationProperties applicationProperties;

    @Inject
    public MyPluginComponentImpl(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public String getName() {
        if (null != applicationProperties) {
            return "myComponent:" + applicationProperties.getDisplayName();
        }
        
        return "myComponent";
    }

    @Override
    public int addNumbers(final int x, final int y) {
        return x + y;
    }
}