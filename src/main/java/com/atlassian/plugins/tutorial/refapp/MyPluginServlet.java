package com.atlassian.plugins.tutorial.refapp;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.tutorial.refapp.domain.entity.Person;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import net.java.ao.DBParam;
import net.java.ao.Query;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import java.util.Optional;

import static com.atlassian.plugins.tutorial.refapp.domain.entity.UserScopedDataMD.USER_KEY_COLUMN;
import static java.util.Optional.ofNullable;

@Scanned
public class MyPluginServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(MyPluginServlet.class);

    @ComponentImport
    private final UserManager userManager;

    @ComponentImport
    private final LoginUriProvider loginUriProvider;

    @ComponentImport
    private final TemplateRenderer templateRenderer;

    @ComponentImport
    private final ActiveObjects activeObjects;

    @Inject
    public MyPluginServlet(final UserManager userManager, final LoginUriProvider loginUriProvider,
                           final TemplateRenderer templateRenderer, final ActiveObjects activeObjects) {
        this.userManager = userManager;
        this.loginUriProvider = loginUriProvider;
        this.templateRenderer = templateRenderer;
        this.activeObjects = activeObjects;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        LOG.info("Incoming GET request.");
        final String userKey = getUserKey(req, res).getStringValue();

        final Optional<Person> person = activeObjects.executeInTransaction(() ->
                ofNullable(activeObjects.find(Person.class, Query.select().where(USER_KEY_COLUMN + " = ?", userKey)))
                    .filter(p -> p.length > 0)
                    .map(p -> p[0]));

        final Map<String, Object> context = new HashMap<>();
        context.put("name", person.map(Person::getName).orElse(null));
        context.put("age", person.map(Person::getAge).orElse(null));

        res.setContentType(MediaType.TEXT_HTML + ";charset=utf-8");
        templateRenderer.render("templates/admin.vm", context, res.getWriter());
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        LOG.info("Incoming POST submit.");
        final String userKey = getUserKey(req, res).getStringValue();

        activeObjects.executeInTransaction(() -> {
            final Person person = ofNullable(activeObjects.find(Person.class, Query.select().where(USER_KEY_COLUMN + " = ?", userKey)))
                    .filter(p -> p.length > 0)
                    .map(p -> p[0])
                    .orElseGet(() -> activeObjects.create(Person.class, new DBParam(USER_KEY_COLUMN, userKey)));

            person.setName(StringUtils.trimToNull(req.getParameter("name")));
            person.setAge(NumberUtils.toInt(StringUtils.trimToNull(req.getParameter("age")), 0));
            person.save();

            return person;
        });

        res.sendRedirect("test");
    }

    private UserKey getUserKey(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        final UserKey userKey = userManager.getRemoteUserKey();
        if (userKey == null || !userManager.isSystemAdmin(userKey)) {
            redirectToLogin(req, res);
            return null;
        }

        return userKey;
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