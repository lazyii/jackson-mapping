package com.demo;

import com.demo.jsonpointer.JacksonUtil;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class JsonPathTest {

    private static String source;

    @BeforeAll
    public static void init() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[7];
        try (InputStream inputStream = JacksonUtil.class.getResourceAsStream("/demo.json")) {
            for (int length; (length = inputStream.read(buffer)) != -1; ) {
                baos.write(buffer, 0, length);
            }
            source = baos.toString();
        } catch (IOException e) {
            System.out.println("读取文件失败");
        }

    }

    @Test
    public void getAllPaths() {
        DocumentContext documentContext = JsonPath.parse(source);
        JsonPath.using(Configuration.builder().build());

        documentContext.read(JsonPath.compile("$"));
        documentContext.read(JsonPath.compile("$..*"));

    }

}
