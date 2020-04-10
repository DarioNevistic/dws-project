package com.dnevi.healthcare.util;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nsoft.chiwava.core.commons.json.JsonMapper;
import com.nsoft.chiwava.core.commons.json.conversion.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public final class JsonMapperWrapper {

    private static final JsonMapper JSON_MAPPER;

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule
                .addDeserializer(LocalDateTime.class, new ParseDeserializer());

        JSON_MAPPER = new JsonMapper.Builder().withModule(javaTimeModule).build();
    }

    private JsonMapperWrapper() {
    }

    public static String toJson(final Object obj) {
        return JSON_MAPPER.toJson(obj);
    }

    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        return JSON_MAPPER.fromJson(jsonString, clazz);
    }

}
