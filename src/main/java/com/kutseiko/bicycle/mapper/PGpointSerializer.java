package com.kutseiko.bicycle.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.postgresql.geometric.PGpoint;

public class PGpointSerializer extends JsonSerializer<PGpoint> {

    @Override
    public void serialize(PGpoint value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("x", value.x);
        gen.writeNumberField("y", value.y);
        gen.writeEndObject();
    }

    @Override
    public Class<PGpoint> handledType() {
        return PGpoint.class;
    }
}
