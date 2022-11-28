package com.demo.resolvers;

import com.demo.MapNode;
import com.demo.NodeType;
import com.demo.resolvers.resolver.ArrayObjectResolver;
import com.demo.resolvers.resolver.ArrayPrimitiveResolver;
import com.demo.resolvers.resolver.NumberResolver;
import com.demo.resolvers.resolver.ObjectResolver;
import com.demo.resolvers.resolver.StringResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class ResolverManager {
    private List<NodeResolver> resolvers;
    
    public ResolverManager() {
        resolvers = new ArrayList<>();
        resolvers.add(new ArrayObjectResolver(this));
        resolvers.add(new ArrayPrimitiveResolver(this));
        resolvers.add(new NumberResolver(this));
        resolvers.add(new ObjectResolver(this));
        resolvers.add(new StringResolver(this));
    }
    
    public NodeResolver matchResolver(MapNode mapNode) {
        Map<NodeType, NodeResolver> resolverMap = this.resolvers.stream().collect(Collectors.toMap(NodeResolver::getType, Function.identity()));
        NodeResolver resolver = resolverMap.get(mapNode.getNodeType());
        return resolver;
    }
    
}
