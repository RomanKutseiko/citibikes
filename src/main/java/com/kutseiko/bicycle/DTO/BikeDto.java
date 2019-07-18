package com.kutseiko.bicycle.DTO;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BikeDto {

    String info;
    @NotNull
    Long stationId;
}
