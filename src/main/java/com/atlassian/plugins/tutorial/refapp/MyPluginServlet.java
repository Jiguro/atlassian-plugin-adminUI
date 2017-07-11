package com.atlassian.plugins.tutorial.refapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyPluginServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(MyPluginServlet.class);

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        LOG.info("Incoming request.");

        resp.setContentType("text/html");
        resp.getWriter().write("<html><body>Hello! You did it.</body></html>");
    }

}