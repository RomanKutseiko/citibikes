package com.kutseiko.bicycle.core.type;

import lombok.Getter;

@Getter
public enum Gender {

    ANOTHER("ANOTHER", 0),
    MALE("MALE", 1),
    FEMALE("FEMALE", 2);

    private String name;
    private Integer code;

    Gender(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    public static Gender valueOf(int code) {
        switch(code) {
            case 1: return MALE;
            case 2: return FEMALE;
            default: return ANOTHER;
        }
    }
}
