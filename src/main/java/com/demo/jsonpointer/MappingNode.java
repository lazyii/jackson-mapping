package com.demo.jsonpointer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.Data;

@Data
public class MappingNode {

    //jsonpath
    private String path;
    //通配符模式 jsonpath  例如   path(/arr/0/name)->wildcardPath(/arr/*/name)
    private String wildcardPath;
    private JsonNodeType nodeType;
    //-1 此节点不是数组类型 ； 0 不全是valueNode；1 全是valueNode
    private int arrayNodeType;

    // 子节点
    private List<MappingNode> children;

    public void setJsonNode(String path, JsonNode jsonNode) {
        this.nodeType = jsonNode.getNodeType();
        if (jsonNode.isArray()) {
            Stream<JsonNode> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(jsonNode.elements(), Spliterator.ORDERED), false);
            stream.collect(Collectors.toList());
            List<JsonNode> nodeList = stream.collect(Collectors.toList());
            boolean allValueNode = nodeList.stream().allMatch(JsonNode::isValueNode);
            arrayNodeType = allValueNode ? 1 : 0;
        } else {
            arrayNodeType = -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MappingNode that = (MappingNode) o;
        return arrayNodeType == that.arrayNodeType && Objects.equals(path, that.path) && Objects.equals(wildcardPath,
                that.wildcardPath) && nodeType == that.nodeType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, wildcardPath, nodeType, arrayNodeType);
    }
}
