package com.demo.resolvers;

import com.demo.MapNode;
import com.demo.NodeType;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;

public interface NodeResolver {
    
    NodeType getType();
    
    JsonNode resolve(MapNode mapNode, Map<Integer, List<MapNode>> nodeMap);
}
