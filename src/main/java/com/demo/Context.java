package com.demo;

import static com.demo.Constant.ROOT_PARENT_NODE_ID;

import com.demo.exception.ExceptionEnum;
import com.demo.exception.TemplateException;
import com.demo.jackson.JsonViews.DetailView;
import com.demo.jackson.ObjectMapperFactory;
import com.demo.resolvers.NodeResolver;
import com.demo.resolvers.ResolverManager;
import com.demo.util.CollectionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Context {
    
    /**
     * 是否开启debug模式，默认不开启。
     * debug模式打印的日志会更详细
     */
    private boolean debug = false;
    private ObjectMapper objectMapper = ObjectMapperFactory.json();
    
    private ResolverManager resolverManager;
    private List<MapNode> mapNodes;
    
    private Context(ResolverManager resolverManager, List<MapNode> mapNodes) {
        this(resolverManager);
        this.mapNodes = mapNodes;
    }
    
    public Context(ResolverManager resolverManager) {
        this.resolverManager = resolverManager;
    }
    
    /**
     * 生成 enjoy 模板
     */
    @SneakyThrows
    public String generateTemplate() {
        Map<Integer, List<MapNode>> nodeMap = mapNodes.stream().collect(Collectors.groupingBy(MapNode::getParentNodeId));
        if (debug) {
            log.info("目标节点的nodeMap: {}", objectMapper.writerWithView(DetailView.class).writeValueAsString(nodeMap));;
        }
        List<MapNode> rootNodes = nodeMap.get(ROOT_PARENT_NODE_ID);
        if (CollectionUtils.isEmpty(rootNodes)) {
            throw new TemplateException(ExceptionEnum.CODE_1000);
        }
        if (debug) {
            //先处理根节点
            MapNode rootNode = rootNodes.get(0);
            NodeResolver resolver = resolverManager.matchResolver(rootNode);
            JsonNode root = resolver.resolve(rootNode, nodeMap);
            log.info("未映射前节点格式化：{}", root.toString());
        }
        return "";
    }
    
    public static Context newContext() {
        return new Context(new ResolverManager());
    }
    
    public static Context newContext(boolean debug) {
        return new Context(new ResolverManager()).setDebug(debug);
    }
    
    public static Context newContext(@NonNull ResolverManager resolverManager) {
        return new Context(resolverManager);
    }
    
    public static Context newContext(@NonNull ResolverManager resolverManager, boolean debug) {
        return new Context(resolverManager).setDebug(debug);
    }
    
    public static Context from(@NonNull Context context) {
        return newContext(context.resolverManager).setDebug(context.debug);
    }
    
    public static Context from(@NonNull Context context, boolean debug) {
        return newContext(context.resolverManager).setDebug(debug);
    }
    
    public Context setResolverManager(ResolverManager resolverManager) {
        this.resolverManager = resolverManager;
        return this;
    }
    
    public Context setMapNodes(List<MapNode> mapNodes) {
        this.mapNodes = mapNodes;
        return this;
    }
    
    public Context setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }
}
