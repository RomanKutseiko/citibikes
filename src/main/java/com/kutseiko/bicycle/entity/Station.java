package com.kutseiko.bicycle.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.postgresql.geometric.PGpoint;

@Data
@Accessors(chain = true)
public class Station {

    @NotNull
    Long id;

    @NotNull
    String name;

    @NotNull
    @JsonIgnoreProperties({"type", "value"})
    PGpoint coordinates;
}
