package com.demo;

public interface Constant {
    
    //根节点 默认id
    int ROOT_NODE_ID = 0;
    //根节点 默认层级
    int ROOT_LEVEL = 0;
    //根节点 默认父级NodeId
    int ROOT_PARENT_NODE_ID = -1;
    //根节点 默认字段名
    String ROOT_NAME = null;
    
    //除根结点外 NODEID计数初始值
    int NODE_ID_INITIAL_VALUE = 1000;
    String DEFAULT_RESOLVER = NodeType.STRING.name();

}
