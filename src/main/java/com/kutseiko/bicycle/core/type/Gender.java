package com.kutseiko.bicycle.core.type;

import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

@Getter
public enum Gender {

    ANOTHER("ANOTHER"),
    FEMALE("FEMALE"),
    MALE("MALE");

    private String name;

    Gender(String name) {
        this.name = name;
    }

    public static Gender getGenderByName(String name) {
        if (EnumUtils.isValidEnumIgnoreCase(Gender.class, name)) {
            return valueOf(name.toUpperCase());
        } else {
            return ANOTHER;
        }
    }
}
