package com.kutseiko.bicycle.entity;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Station {

    @NotNull
    Long id;

    @NotNull
    String name;

    @NotNull
    Double latitude;

    @NotNull
    Double longitude;
}
