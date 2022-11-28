package com.demo.resolvers.resolver;

import com.demo.MapNode;
import com.demo.NodeType;
import com.demo.resolvers.AbstractNodeResolver;
import com.demo.resolvers.ResolverManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NumericNode;
import java.util.List;
import java.util.Map;

public class NumberResolver extends AbstractNodeResolver {
    
    public NumberResolver() {
        super();
    }
    
    public NumberResolver(ResolverManager resolverManager) {
        super(resolverManager);
    }
    
    @Override
    public NodeType getType() {
        return NodeType.NUMBER;
    }
    
    @Override
    public JsonNode resolve(MapNode mapNode, Map<Integer, List<MapNode>> nodeMap) {
        NumericNode numericNode = JsonNodeFactory.instance.numberNode(2);
        return numericNode;
    }
}
