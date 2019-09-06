package com.kutseiko.bicycle.core.type;

import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

@Getter
public enum UserType {

    CUSTOMER("CUSTOMER"),
    UNDEFINED("UNDEFINED"),
    SUBSCRIBER("SUBSCRIBER");

    private String name;

    UserType(String name) {
        this.name = name;
    }


    public static UserType getUserTypeByName(String name) {
        if (EnumUtils.isValidEnumIgnoreCase(UserType.class, name)) {
            return valueOf(name.toUpperCase());
        }
        return SUBSCRIBER;
    }
}
