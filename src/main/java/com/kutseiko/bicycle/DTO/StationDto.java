package com.kutseiko.bicycle.DTO;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StationDto {

    @NotNull
    String name;

    @NotNull
    Double latitude;

    @NotNull
    Double longitude;
}
