package com.demo.jsonpointer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class JacksonPointerTest {

    private static String source;
    private static String mapping;

    @BeforeAll
    public static void init() {
        String demoPath = "/demo.json";
        String mappingPath = "/mapping.json";
        source = readFile(demoPath);
        mapping = readFile(mappingPath);
    }

    private static String readFile(String relativePath) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[7];
        try (InputStream inputStream = JacksonUtil.class.getResourceAsStream(relativePath)) {
            for (int length; (length = inputStream.read(buffer)) != -1; ) {
                baos.write(buffer, 0, length);
            }
            return baos.toString();
        } catch (IOException e) {
            System.out.println("读取文件失败");
        }
        return null;
    }

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
    public void jsonPointer() {
        try {

            JsonNode jsonNode = JacksonUtil.readTree(source);
            JsonPointer ptr = JsonPointer.compile("/extInfo/email");
            jsonNode = jsonNode.at(ptr);
            System.out.println(jsonNode);
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
    public void getAllKeysWithJsonParser() {
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
            Map<String, String> keyMapping = keys.stream().collect(Collectors.toMap(x -> x, x -> x + "-target", (k1, k2) -> k2));
            //输出转换后的key
            System.out.println(JacksonUtil.getObjectMapper().writeValueAsString(keyMapping));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取所有 key
     */
    @Test
    public void getAllKeysWithJsonNode() {
        try {
            List<String> keys = new ArrayList<>();
            JsonNode jsonNode = JacksonUtil.readTree(source);
            keys = JacksonUtil.getAllKeys(jsonNode);
            System.out.println(keys);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 参数名变更测试
     */
    @Test
    public void replaceKeyTest() {
        try {
            JsonNode jsonNode = JacksonUtil.readTree(source);
            Map<String, String> keyMapping = JacksonUtil.getObjectMapper().readValue(mapping, new TypeReference<Map<String, String>>() {});
            JacksonUtil.replaceKey(jsonNode, keyMapping);
            System.out.println(JacksonUtil.getObjectMapper().writeValueAsString(jsonNode));
            System.out.println(jsonNode.asText());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
