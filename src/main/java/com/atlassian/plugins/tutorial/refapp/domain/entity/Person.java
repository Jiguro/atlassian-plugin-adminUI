package com.atlassian.plugins.tutorial.refapp.domain.entity;

import net.java.ao.Entity;

public interface Person extends Entity, UserScopedData {

    enum Gender {
        MALE, FEMALE, OTHER
    }

    String getName();

    void setName(String name);

    Integer getAge();

    void setAge(Integer age);

    Gender getGender();

    void setGender(Gender gender);

}
