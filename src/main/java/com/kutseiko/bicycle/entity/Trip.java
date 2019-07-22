package com.kutseiko.bicycle.entity;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Trip {

    @NotNull
    Long id;

    @NotNull
    User user;

    @NotNull
    Bike bike;

    @NotNull
    Station startStation;

    Station endStation;

    @NotNull
    LocalDateTime startTime;

    LocalDateTime endTime;
}
