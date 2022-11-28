package com.demo.resolvers.resolver;

import com.demo.MapNode;
import com.demo.NodeType;
import com.demo.resolvers.AbstractNodeResolver;
import com.demo.resolvers.NodeResolver;
import com.demo.resolvers.ResolverManager;
import com.demo.util.CollectionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.Map;

public class ArrayObjectResolver extends AbstractNodeResolver {
    
    public ArrayObjectResolver() {
        super();
    }
    
    public ArrayObjectResolver(ResolverManager resolverManager) {
        super(resolverManager);
    }
    
    @Override
    public NodeType getType() {
        return NodeType.ARRAY_OBJECT;
    }
    
    @Override
    public JsonNode resolve(MapNode mapNode, Map<Integer, List<MapNode>> nodeMap) {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        List<MapNode> subNodeList = nodeMap.get(mapNode.getNodeId());
        if (CollectionUtils.isNotEmpty(subNodeList)) {
            ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
            for (MapNode subNode : subNodeList) {
                NodeResolver resolver = resolverManager.matchResolver(subNode);
                objectNode.set(subNode.getFieldName(), resolver.resolve(subNode, nodeMap));
            }
            arrayNode.add(objectNode);
        }
        return arrayNode;
    }
}
