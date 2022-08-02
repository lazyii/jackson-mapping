package com.demo.jsonpointer;

import static com.fasterxml.jackson.core.JsonPointer.SEPARATOR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    public static JsonNode readTree(String jsonStr) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(jsonStr);
        return jsonNode;
    }

    public static Map<String, JsonNode> getAllNodesAsMap(JsonNode rootNode) {
        return getAllNodesAsMap(rootNode, "");
    }
    public static Map<String, JsonNode> getAllNodesAsMap(JsonNode jsonNode, String prefix) {
        Map<String, JsonNode> result = new HashMap<>();
        jsonNode.fields().forEachRemaining(x -> {
            String newPath = prefix == "" ? SEPARATOR + x.getKey() : prefix + SEPARATOR + x.getKey();
            JsonNode node = x.getValue();
            if (node.isValueNode()) {
                result.put(newPath, node);
            } else if (node.isArray()) {
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


}
