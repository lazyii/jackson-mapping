package com.demo.resolvers.resolver;

import com.demo.MapNode;
import com.demo.NodeType;
import com.demo.resolvers.AbstractNodeResolver;
import com.demo.resolvers.ResolverManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import java.util.List;
import java.util.Map;

public class StringResolver extends AbstractNodeResolver {
    
    public StringResolver() {
        super();
    }
    
    public StringResolver(ResolverManager resolverManager) {
        super(resolverManager);
    }
    
    @Override
    public NodeType getType() {
        return NodeType.STRING;
    }
    
    @Override
    public JsonNode resolve(MapNode mapNode, Map<Integer, List<MapNode>> nodeMap) {
        TextNode textNode = JsonNodeFactory.instance.textNode(null);
        return textNode;
    }
}
