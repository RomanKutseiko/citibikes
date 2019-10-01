package com.kutseiko.bicycle.entity;

import com.kutseiko.bicycle.annotations.ColumnName;
import com.kutseiko.bicycle.annotations.CustomJoinTable;
import com.kutseiko.bicycle.annotations.TableName;
import com.kutseiko.bicycle.core.type.db.tables.BikeTable;
import com.kutseiko.bicycle.core.type.db.tables.StationTable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName(name = BikeTable.TABLE)
public class Bike {

    @NotNull
    @ColumnName(name = BikeTable.ID)
    Long id;

    @NotNull
    @CustomJoinTable(tableName = StationTable.TABLE, columnName = BikeTable.STATION_ID)
    Station station;

    @ColumnName(name = BikeTable.INFO)
    String info;

    @ColumnName(name = BikeTable.CREATED_DATE)
    LocalDateTime createdDate;

    @ColumnName(name = BikeTable.UPDATED_DATE)
    LocalDateTime updatedDate;
}
