package com.demo;

import com.demo.jsonpointer.JacksonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.JsonNodeValueResolver;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class HandleBarsTest {

    private static Handlebars handlebars;
    private static Context defaultContext;
    private static String source;

    @BeforeAll
    public static void init() {
        TemplateLoader loader = new FileTemplateLoader("d://srv");
        handlebars = new Handlebars();
        defaultContext = Context.newBuilder(null).push(JsonNodeValueResolver.INSTANCE).build();
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
    public void handlebars_jackson_Test() {
        try {
            Template template = handlebars.compile("hello_jackson2");
            JsonNode jsonNode = JacksonUtil.readTree(source);
            Context context = Context.newContext(defaultContext, jsonNode);
            String result = template.apply(context);
            System.out.println(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
