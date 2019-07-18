package com.kutseiko.bicycle.entity;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Trip {

    @NotNull
    Long id;

    @NotNull
    User userId;

    @NotNull
    Bike bike;

    @NotNull
    Station startStation;

    Station endStation;

    @NotNull
    LocalDateTime startTime;

    LocalDateTime endTime;
}
