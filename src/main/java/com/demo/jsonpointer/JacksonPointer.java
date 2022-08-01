package com.demo.jsonpointer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonPointer {

    public static ObjectMapper objectMapper = new ObjectMapper().enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    public JsonNode getAllJsonPointer(String jsonStr) throws JsonProcessingException {
        return objectMapper.readTree(jsonStr);
    }
}
