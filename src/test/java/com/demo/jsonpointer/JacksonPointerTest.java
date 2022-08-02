package com.demo.jsonpointer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class JacksonPointerTest {

    private static String source = "{\"taxnum\":\"taxnum0000001\",\"companyName\":\"测试有限公司\",\"extInfo\":{\"email\":\"abc@abc.com\",\"phone\":\"13112341234\"},\"deptIds\":[1,2,3,4],\"others\":[{\"key1\":\"value10\",\"key2\":\"value20\"},{\"key1\":\"value11\",\"key2\":\"value21\"}]}";

    @Test
    public void jsonNode() {
        try {
            JsonNode jsonNode = JacksonUtil.readTree(source);
            // 获取 key:value键值对。 arrayNode.fields() 为空，因为没有key
            jsonNode.fields().forEachRemaining(x -> System.out.println(x));
            // 获取 node内所有元素对象。 arrayNode.elements() 得到数组内的
            jsonNode.elements().forEachRemaining(x -> System.out.println(x));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getAllNodesAsMapTest() {
        try {
            JsonNode rootNode = JacksonUtil.readTree(source);
            Map<String, JsonNode> map = JacksonUtil.getAllNodesAsMap(rootNode);
            System.out.println(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getAllJsonNodes() {
        try {
            JsonNode rootNode = JacksonUtil.readTree(source);
            rootNode.fields().forEachRemaining(x -> {
                System.out.println(x.getKey());
                JsonNode node = x.getValue();
                System.out.println(node.traverse());
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void jsonParser() {
        try {

            List<String> keys = new ArrayList<>();
            JsonFactory factory = new JsonFactory();
            JsonParser jsonParser = factory.createParser(source);
            while (!jsonParser.isClosed()) {
                JsonToken jsonToken = jsonParser.nextToken();
                System.out.println(jsonToken);
                if (jsonToken == JsonToken.FIELD_NAME) {
                    keys.add((jsonParser.getCurrentName()));
                }
            }
            System.out.println(keys);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void jsonParserInJsonNode() {
        try {
            List<String> keys = new ArrayList<>();
            JsonNode jsonNode = JacksonUtil.readTree(source);
            JsonParser jsonParser = jsonNode.traverse();
            while (!jsonParser.isClosed()) {
                JsonToken jsonToken = jsonParser.nextToken();
                System.out.println(jsonToken);
                if (jsonToken == JsonToken.FIELD_NAME) {
                    keys.add((jsonParser.getCurrentName()));
                }
            }
            System.out.println(keys);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
