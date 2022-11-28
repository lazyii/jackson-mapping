package com.demo.resolvers.resolver;

import com.demo.MapNode;
import com.demo.NodeType;
import com.demo.resolvers.AbstractNodeResolver;
import com.demo.resolvers.ResolverManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.util.List;
import java.util.Map;

public class ArrayPrimitiveResolver extends AbstractNodeResolver {
    
    public ArrayPrimitiveResolver() {
        super();
    }
    
    public ArrayPrimitiveResolver(ResolverManager resolverManager) {
        super(resolverManager);
    }
    
    @Override
    public NodeType getType() {
        return NodeType.ARRAY_PRIMITIVE;
    }
    
    @Override
    public JsonNode resolve(MapNode mapNode, Map<Integer, List<MapNode>> nodeMap) {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode(2);
        return arrayNode;
    }
}
