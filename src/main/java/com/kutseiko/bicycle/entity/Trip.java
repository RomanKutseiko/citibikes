package com.kutseiko.bicycle.entity;

import com.kutseiko.bicycle.annotations.ColumnName;
import com.kutseiko.bicycle.annotations.CustomJoinTable;
import com.kutseiko.bicycle.annotations.TableName;
import com.kutseiko.bicycle.core.type.db.tables.AppUserTable;
import com.kutseiko.bicycle.core.type.db.tables.BikeTable;
import com.kutseiko.bicycle.core.type.db.tables.StationTable;
import com.kutseiko.bicycle.core.type.db.tables.TripTable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName(name = TripTable.TABLE)
public class Trip {

    @NotNull
    @ColumnName(name = TripTable.ID)
    Long id;

    @NotNull
    @CustomJoinTable(tableName = AppUserTable.TABLE, columnName = TripTable.USER_ID)
    User user;

    @NotNull
    @CustomJoinTable(tableName = BikeTable.TABLE, columnName = TripTable.BIKE_ID)
    Bike bike;

    @NotNull
    @CustomJoinTable(tableName = StationTable.TABLE, columnName = TripTable.START_STATION_ID)
    Station startStation;

    @CustomJoinTable(tableName = StationTable.TABLE, columnName = TripTable.END_STATION_ID)
    Station endStation;

    @NotNull
    @ColumnName(name = TripTable.START_TIME)
    LocalDateTime startTime;

    @ColumnName(name = TripTable.END_TIME)
    LocalDateTime endTime;
}
