package com.atlassian.plugins.tutorial.refapp;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import org.apache.commons.lang3.math.NumberUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.atlassian.plugins.tutorial.refapp.MyPluginServlet.PLUGIN_STORAGE_KEY;

@Path("/")
@Scanned
public class ConfigResource {

    @ComponentImport
    private final UserManager userManager;

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    @ComponentImport
    private final TransactionTemplate transactionTemplate;

    @Inject
    public ConfigResource(final UserManager userManager, final PluginSettingsFactory pluginSettingsFactory,
                          final TransactionTemplate transactionTemplate) {
        this.userManager = userManager;
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.transactionTemplate = transactionTemplate;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context final HttpServletRequest request) {
        final UserKey userKey = userManager.getRemoteUserKey();
        if (userKey == null || !userManager.isSystemAdmin(userKey)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        return Response.ok(transactionTemplate.execute(() -> {
            final PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();

            final Config config = new Config();
            config.setName((String) pluginSettings.get(PLUGIN_STORAGE_KEY + ".name"));
            config.setAge(NumberUtils.toInt((String) pluginSettings.get(PLUGIN_STORAGE_KEY + ".age")));

            return config;
        })).build();
    }


    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Config {

        @XmlElement
        private String name;

        @XmlElement
        private int age;

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(final int age) {
            this.age = age;
        }
    }
}
