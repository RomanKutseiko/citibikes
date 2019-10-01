package com.kutseiko.bicycle.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.postgresql.geometric.PGpoint;

@Data
public class StationDto {

    @NotNull
    String name;

    @NotNull
    @JsonIgnoreProperties({ "type", "value" })
    PGpoint coordinates;

}
