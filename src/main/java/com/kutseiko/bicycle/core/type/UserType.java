package com.kutseiko.bicycle.core.type;

import lombok.Getter;

@Getter
public enum UserType {

    CUSTOMER("CUSTOMER"),
    SUBSCRIBER("SUBSCRIBER");

    private String name;

    UserType(String name) {
        this.name = name;
    }


    public static UserType getUserTypeByName(String name) {
        if ("CUSTOMER".equals(name.toUpperCase())) {
            return CUSTOMER;
        }
        return SUBSCRIBER;
    }
}
