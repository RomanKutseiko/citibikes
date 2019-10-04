package com.kutseiko.bicycle.config;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kutseiko.bicycle.mapper.PGpointSerializer;
import java.text.SimpleDateFormat;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new Jdk8Module())
            .registerModule(new SimpleModule("MapperModule", new Version(1, 0, 0, null, null, null)).addSerializer(new PGpointSerializer()))
            .registerModule(new JavaTimeModule())
            .setDateFormat(new SimpleDateFormat()); //formats date to YYYY-MM-ddThh:mm:ss.ms
    }
}
