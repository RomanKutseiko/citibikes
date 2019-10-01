package com.kutseiko.bicycle.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kutseiko.bicycle.annotations.ColumnName;
import com.kutseiko.bicycle.annotations.TableName;
import com.kutseiko.bicycle.core.type.db.tables.StationTable;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.postgresql.geometric.PGpoint;

@Data
@Accessors(chain = true)
@TableName(name = StationTable.TABLE)
public class Station {

    @NotNull
    @ColumnName(name = StationTable.ID)
    Long id;

    @NotNull
    @ColumnName(name = StationTable.NAME)
    String name;

    @NotNull
    @JsonIgnoreProperties({"type", "value"})
    @ColumnName(name = StationTable.COORDINATES)
    PGpoint coordinates;
}
