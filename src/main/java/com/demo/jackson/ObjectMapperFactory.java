package com.demo.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.math.BigInteger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectMapperFactory {
    
    public static ObjectMapper json() {
        return create(null);
    }
    
    private static ObjectMapper create(JsonFactory jsonFactory) {
        ObjectMapper mapper = jsonFactory == null ? new ObjectMapper() : new ObjectMapper(jsonFactory);
        
        //SerializationFeature
        mapper.configure(Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        
        //DeserializationFeature
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
        //module
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        //simpleModule.addSerializer(BigDecimal.class, ToNumberStringSerializer.INSTANCE);
        //simpleModule.addSerializer(Date.class, ToDateStringSerializer.INSTANCE);
        mapper.registerModules(simpleModule);
        //解决 localDateTime/jodaTime等类型问题
        try {
            Class clazz = Class.forName("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule");
            Module javaTimeModule = (Module) clazz.newInstance();
            mapper.registerModule(javaTimeModule);
        } catch (Exception e) {
            log.warn("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule not found, skip");
        }
    
        return mapper;
    }
}