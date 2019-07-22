package com.kutseiko.bicycle.DTO;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class TripDto {

    @NotNull
    Long userId;

    @NotNull
    Long bikeId;

    @NotNull
    Long startStationId;

    Long endStationId;

    @NotNull
    LocalDateTime startTime;

    LocalDateTime endTime;
}
