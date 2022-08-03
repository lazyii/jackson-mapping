package com.demo.jsonpointer;

import static com.fasterxml.jackson.core.JsonPointer.SEPARATOR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JacksonUtil {

    private static Logger logger = LoggerFactory.getLogger(JacksonUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper().enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);


    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static JsonNode readTree(String jsonStr) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(jsonStr);
        return jsonNode;
    }

    /**
     * 根据全局配置，替换所有节点的key
     * @return
     */
    public static void replaceKey(JsonNode jsonNode, Map<String, String> namePair) {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Entry<String, JsonNode>> iterator = objectNode.fields();
            Map<String, JsonNode> addMap = new HashMap<>();
            while (iterator.hasNext()) {
                Entry<String, JsonNode> entry = iterator.next();
                String key = entry.getKey();
                JsonNode node = entry.getValue();
                String targetKey = namePair.get(key);
                if (targetKey != null) {
                    iterator.remove();
                    addMap.put(targetKey, node);
                    //objectNode.remove(key);
                    //objectNode.set(targetKey, node);
                    if (node.isContainerNode()) {
                        replaceKey(node, namePair);
                    }
                }
            }
            objectNode.setAll(addMap);
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                replaceKey(arrayNode.get(i), namePair);
            }
        }
    }

    public static Map<String, JsonNode> getAllNodesAsMap(JsonNode rootNode) {
        return getAllNodesAsMap(rootNode, "");
    }
    public static Map<String, JsonNode> getAllNodesAsMap(JsonNode jsonNode, String prefix) {
        Map<String, JsonNode> result = new HashMap<>();
        jsonNode.fields().forEachRemaining(x -> {
            String newPath = prefix == "" ? SEPARATOR + x.getKey() : prefix + SEPARATOR + x.getKey();
            JsonNode node = x.getValue();
            result.put(newPath, node);
            if (node.isArray()) {
                Stream<JsonNode> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(node.elements(), Spliterator.ORDERED), false);
                List<JsonNode> nodeList = stream.collect(Collectors.toList());
                boolean allValueNode = nodeList.stream().allMatch(JsonNode::isValueNode);
                if (allValueNode) {
                    result.put(newPath, node);
                } else {
                    for (int i = 0; i < nodeList.size(); i++) {
                        Map<String, JsonNode> nodeMap = getAllNodesAsMap(nodeList.get(i), newPath + SEPARATOR + i);
                        result.putAll(nodeMap);
                    }
                }
            } else if (node.getNodeType() == JsonNodeType.OBJECT) {
                Map<String, JsonNode> nodeMap = getAllNodesAsMap(node, newPath);
                result.putAll(nodeMap);
            } else {
                logger.debug("getAllNodesAsMap: node can not convert.  key: {}  value: ", x.getKey(), x.getValue());
            }
        });
        return result;
    }

    public static List<String> getAllKeys(JsonNode jsonNode) {
        List<String> keys = new ArrayList<>();
        jsonNode.fields().forEachRemaining(x -> {
            JsonNode node = x.getValue();
            keys.add(x.getKey());
            if (node.isArray()) {
                Stream<JsonNode> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(node.elements(), Spliterator.ORDERED), false);
                List<JsonNode> nodeList = stream.collect(Collectors.toList());
                boolean allValueNode = nodeList.stream().allMatch(JsonNode::isValueNode);
                if (!allValueNode) {
                    for (int i = 0; i < nodeList.size(); i++) {
                        keys.addAll(getAllKeys(nodeList.get(i)));
                    }
                }
            } else if (node.getNodeType() == JsonNodeType.OBJECT) {
                keys.addAll(getAllKeys(node));
            } else {
                logger.debug("getAllNodesAsMap: node can not convert.  key: {}  value: ", x.getKey(), x.getValue());
            }
        });
        return keys;
    }
}
