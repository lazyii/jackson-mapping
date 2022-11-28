package com.demo.resolvers.resolver;

import static com.demo.NodeType.OBJECT;

import com.demo.MapNode;
import com.demo.NodeType;
import com.demo.resolvers.AbstractNodeResolver;
import com.demo.resolvers.NodeResolver;
import com.demo.resolvers.ResolverManager;
import com.demo.util.CollectionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.Map;

public class ObjectResolver extends AbstractNodeResolver {
    
    public ObjectResolver() {
        super();
    }
    
    public ObjectResolver(ResolverManager resolverManager) {
        super(resolverManager);
    }
    
    @Override
    public NodeType getType() {
        return OBJECT;
    }
    
    @Override
    public JsonNode resolve(MapNode mapNode, Map<Integer, List<MapNode>> nodeMap) {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        List<MapNode> subNodeList = nodeMap.get(mapNode.getNodeId());
        if (CollectionUtils.isNotEmpty(subNodeList)) {
            for (MapNode subNode : subNodeList) {
                NodeResolver resolver = resolverManager.matchResolver(subNode);
                objectNode.set(subNode.getFieldName(), resolver.resolve(subNode, nodeMap));
            }
        }
        return objectNode;
    }
}
