package com.kutseiko.bicycle.entity;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Bike {

    @NotNull
    Long id;

    @NotNull
    Station station;

    String info;
}
