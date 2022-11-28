package com.demo;

import static com.demo.Constant.NODE_ID_INITIAL_VALUE;
import static com.demo.Constant.ROOT_LEVEL;
import static com.demo.Constant.ROOT_NAME;
import static com.demo.Constant.ROOT_NODE_ID;
import static com.demo.Constant.ROOT_PARENT_NODE_ID;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.jsonpointer.JacksonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.jfinal.template.Directive;
import com.jfinal.template.Engine;
import com.jfinal.template.Env;
import com.jfinal.template.Template;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.ParseException;
import com.jfinal.template.stat.Scope;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EnjoyTest {
    
    
    private static Engine engine = Engine.use();
    private static String source;
    
    
    @BeforeAll
    public static void init() {
        Engine.setFastMode(true);
        Engine.setChineseExpression(true);
        engine.addDirective("value", ValueDirective.class);
        engine.setDevMode(true);
        engine.setToClassPathSourceFactory();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[7];
        try (InputStream inputStream = JacksonUtil.class.getResourceAsStream("/demo.json")) {
            for (int length; (length = inputStream.read(buffer)) != -1; ) {
                baos.write(buffer, 0, length);
            }
            source = baos.toString();
        } catch (IOException e) {
            System.out.println("读取文件失败");
        }
        
    }
    
    @Test
    public void enjoy_parse() {
        try {
            // nodeId 0 默认为root,所以其他的从 100 开始
            AtomicInteger autoId = new AtomicInteger(NODE_ID_INITIAL_VALUE);
            
            JsonNode jsonNode = JacksonUtil.readTree(source);
            System.out.println(jsonNode);
            Spliterator<String> spliterator = Spliterators.spliteratorUnknownSize(jsonNode.fieldNames(), Spliterator.ORDERED);
            List<String> keyList = StreamSupport.stream(spliterator, false).collect(Collectors.toList());
            System.out.println(keyList);
    
            MapNode rootNode = new MapNode();
            rootNode.setNodeId(ROOT_NODE_ID);
            rootNode.setLevel(ROOT_LEVEL);
            rootNode.setParentNodeId(ROOT_PARENT_NODE_ID);
            rootNode.setNodeType(getNodeType(jsonNode));
            rootNode.setFieldName(ROOT_NAME);
            rootNode.setJsonNode(jsonNode);
            
            List<MapNode> nodeList = new ArrayList<>();
            nodeList.add(rootNode);
            
            parseNodeRecursion(nodeList, Arrays.asList(rootNode), autoId);
            
            System.out.println(JSON.toJSONString(nodeList));
    
            Context context = Context.newContext(true).setMapNodes(nodeList);
            String r = context.generateTemplate();
            System.out.println(r);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private void parseNodeRecursion(List<MapNode> nodeList, List<MapNode> tmpList, AtomicInteger autoId) {
        for (MapNode mapNode : tmpList) {
            tmpList = jsonNode2MapNodeList(mapNode.getJsonNode(), mapNode.getLevel(), mapNode.getNodeId(), autoId);
            nodeList.addAll(tmpList);
            tmpList = tmpList.stream().filter(x -> x.getNodeType() == NodeType.OBJECT || x.getNodeType() == NodeType.ARRAY_OBJECT).collect(Collectors.toList());
            parseNodeRecursion(nodeList, tmpList, autoId);
        }
    }
    
    /**
     * @param pNode
     * @return
     */
    private List<MapNode> jsonNode2MapNodeList(JsonNode pNode, int pLevel, int parentNodeId, AtomicInteger autoId) {
        Objects.requireNonNull(pNode, "pNode can not be null");
        if (pNode.isObject()) {
            Spliterator<String> spliterator = Spliterators.spliteratorUnknownSize(pNode.fieldNames(), Spliterator.ORDERED);
            List<String> fieldNameList = StreamSupport.stream(spliterator, false).collect(Collectors.toList());
            List<MapNode> nodeList = new ArrayList<>();
            for (String fieldName : fieldNameList) {
                JsonNode cNode = pNode.get(fieldName);
                MapNode mapNode = new MapNode();
                mapNode.setNodeId(autoId == null ? -1 : autoId.getAndIncrement());
                mapNode.setLevel(pLevel + 1);
                mapNode.setFieldName(fieldName);
                mapNode.setParentNodeId(parentNodeId);
                mapNode.setNodeType(getNodeType(cNode));
                mapNode.setJsonNode(cNode);
                nodeList.add(mapNode);
            }
            return nodeList;
        } else if(pNode.isArray()) {
            //如果是ARRAY_OBJECT
            List<MapNode> nodeList = new ArrayList<>();
            int size = pNode.size();
            List<MapNode> duplicatedNodeList = IntStream.range(0, size).mapToObj(x -> pNode.get(x))
                    .flatMap(node -> jsonNode2MapNodeList(node, pLevel, parentNodeId, null).stream())
                    .collect(Collectors.toList());
            Map<String, List<MapNode>> duplicatedNodeMap = duplicatedNodeList.stream().collect(Collectors.groupingBy(MapNode::getFieldName));
            List<String> fieldNameList = duplicatedNodeMap.keySet().stream().collect(Collectors.toList());
            for (String fieldName : fieldNameList) {
                List<MapNode> mapNodes = duplicatedNodeMap.get(fieldName);
                MapNode mapNode = mapNodes.get(0);
                //array_object类的nodeId特殊处理一下，防止发生id跳过的问题。
                mapNode.setNodeId(autoId.getAndIncrement());
                nodeList.add(mapNode);
            }
            return nodeList;
        }
        return new ArrayList<>();
    }
    
    private NodeType getNodeType(JsonNode jsonNode) {
        if (jsonNode.isObject()) {
            return NodeType.OBJECT;
        } else if (jsonNode.isArray()) {
            // false: 原始类型array, true: 此array中有object
            boolean objectArray = StreamSupport.stream(Spliterators.spliteratorUnknownSize(jsonNode.elements(), Spliterator.ORDERED), false).allMatch(x -> {
                NodeType type = getNodeType(x);
                return type == NodeType.OBJECT;
            });
            return objectArray ? NodeType.ARRAY_OBJECT : NodeType.ARRAY_PRIMITIVE;
        } else if (jsonNode.isNumber()) {
            return NodeType.NUMBER;
        } else {
            return NodeType.STRING;
        }
    }
    
    @Test
    public void enjoy_jackson_Test() {
        try {
            Template template = engine.getTemplate("hello.enjoy");
            JSONObject json = JSON.parseObject(source);
            String result = template.renderToString(json);
            System.out.println(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
    
    public static class ValueDirective extends Directive {
        
        private Expr valueExpr;
        private Expr patternExpr;
        
        public void setExprList(ExprList exprList) {
            int paraNum = exprList.length();
            if (paraNum == 0) {
                throw new ParseException("The parameter of #number directive can not be blank", location);
            }
            if (paraNum > 2) {
                throw new ParseException("Wrong number parameter of #number directive, two parameters allowed at most", location);
            }
            
            valueExpr = exprList.getExpr(0);
            patternExpr = (paraNum == 1 ? null : exprList.getExpr(1));
        }
        
        public void exec(Env env, Scope scope, Writer writer) {
            Object value = valueExpr.eval(scope);
            if (value == null) {
                write(writer, "null");
            } else {
                if (value instanceof String) {
                    value = "\"" + value + "\"";
                }
                write(writer, value.toString());
            }
        }
    }
    
    
}
