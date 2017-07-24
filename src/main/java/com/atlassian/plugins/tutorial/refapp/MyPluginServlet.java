package com.atlassian.plugins.tutorial.refapp;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Scanned
public class MyPluginServlet extends HttpServlet {

    static final String PLUGIN_STORAGE_KEY = "com.atlassian.plugins.tutorial.refapp.adminui";

    private static final Logger LOG = LoggerFactory.getLogger(MyPluginServlet.class);

    @ComponentImport
    private final UserManager userManager;

    @ComponentImport
    private final LoginUriProvider loginUriProvider;

    @ComponentImport
    private final TemplateRenderer templateRenderer;

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    @Inject
    public MyPluginServlet(final UserManager userManager, final LoginUriProvider loginUriProvider,
                           final TemplateRenderer templateRenderer, final PluginSettingsFactory pluginSettingsFactory) {
        this.userManager = userManager;
        this.loginUriProvider = loginUriProvider;
        this.templateRenderer = templateRenderer;
        this.pluginSettingsFactory = pluginSettingsFactory;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        LOG.info("Incoming request.");

        final UserKey userKey = userManager.getRemoteUserKey();
        if (userKey == null || !userManager.isSystemAdmin(userKey)) {
            redirectToLogin(req, res);
            return;
        }

        final PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
        final Map<String, Object> context = new HashMap<>();
        context.put("name", pluginSettings.get(PLUGIN_STORAGE_KEY + ".name"));
        context.put("age", pluginSettings.get(PLUGIN_STORAGE_KEY + ".age"));

        res.setContentType(MediaType.TEXT_HTML + ";charset=utf-8");
        templateRenderer.render("templates/admin.vm", context, res.getWriter());
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        final PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();

        pluginSettings.put(PLUGIN_STORAGE_KEY + ".name", StringUtils.trimToNull(req.getParameter("name")));
        pluginSettings.put(PLUGIN_STORAGE_KEY + ".age", StringUtils.trimToNull(req.getParameter("age")));
        res.sendRedirect("test");
    }

    private void redirectToLogin(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        response.sendRedirect(loginUriProvider.getLoginUri(getUri(request)).toASCIIString());
    }

    private URI getUri(final HttpServletRequest request) {
        final StringBuffer builder = request.getRequestURL();
        if (StringUtils.isNotEmpty(request.getQueryString())) {
            builder.append('?');
            builder.append(request.getQueryString());
        }
        return URI.create(builder.toString());
    }
}