package com.atlassian.plugins.tutorial.refapp.domain.entity;

import net.java.ao.schema.NotNull;
import net.java.ao.schema.Unique;

public interface UserScopedData {

    @NotNull
    @Unique
    String getUserKey();

    void setUserKey(String userKey);

}
