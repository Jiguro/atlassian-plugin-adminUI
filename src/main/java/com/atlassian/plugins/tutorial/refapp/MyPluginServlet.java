package com.atlassian.plugins.tutorial.refapp;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@Scanned
public class MyPluginServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(MyPluginServlet.class);

    @ComponentImport
    private final UserManager userManager;

    @ComponentImport
    private final LoginUriProvider loginUriProvider;

    @Inject
    public MyPluginServlet(final UserManager userManager, final LoginUriProvider loginUriProvider) {
        this.userManager = userManager;
        this.loginUriProvider = loginUriProvider;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        LOG.info("Incoming request.");

        final UserProfile user = userManager.getRemoteUser(req);
        if (user == null || !userManager.isSystemAdmin(user.getUserKey())) {
            redirectToLogin(req, resp);
            return;
        }
        resp.setContentType("text/html");
        resp.getWriter().write(String.format("<html><body>Hi again, %s! Looking good ^_^</body></html>", user.getFullName()));
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