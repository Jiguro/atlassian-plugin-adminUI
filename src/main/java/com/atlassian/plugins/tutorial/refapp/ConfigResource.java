package com.atlassian.plugins.tutorial.refapp;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.tutorial.refapp.domain.entity.Person;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import net.java.ao.Query;

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
import java.util.Optional;

import static com.atlassian.plugins.tutorial.refapp.domain.entity.UserScopedDataMD.USER_KEY_COLUMN;
import static java.util.Optional.ofNullable;

@Path("/")
@Scanned
public class ConfigResource {

    @ComponentImport
    private final UserManager userManager;

    @ComponentImport
    private final ActiveObjects activeObjects;

    @Inject
    public ConfigResource(final UserManager userManager, final ActiveObjects activeObjects) {
        this.userManager = userManager;
        this.activeObjects = activeObjects;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context final HttpServletRequest request) {
        final UserKey userKey = userManager.getRemoteUserKey();
        if (userKey == null || !userManager.isSystemAdmin(userKey)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        final Optional<Person> person = activeObjects.executeInTransaction(() ->
                ofNullable(activeObjects.find(Person.class, Query.select().where(USER_KEY_COLUMN + " = ?", userKey.getStringValue())))
                        .filter(p -> p.length > 0)
                        .map(p -> p[0]));

        return Response.ok(new Config()
                    .setName(person.map(Person::getName).orElse(null))
                    .setAge(person.map(Person::getAge).orElse(null)))
                .build();
    }


    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Config {

        @XmlElement
        private String name;

        @XmlElement
        private Integer age;

        public String getName() {
            return name;
        }

        public Config setName(final String name) {
            this.name = name;
            return this;
        }

        public Integer getAge() {
            return age;
        }

        public Config setAge(final Integer age) {
            this.age = age;
            return this;
        }
    }
}
