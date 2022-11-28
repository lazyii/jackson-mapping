package com.demo;

import java.util.Arrays;
import java.util.List;

public enum NodeType {
    STRING,
    NUMBER,
    OBJECT,
    /**
     * 数组且数组中的元素都是原始类型。例如：[1,2,3]
     */
    ARRAY_PRIMITIVE,
    /**
     * 数组且数组中的元素有OBJECT。例如：[1,2,{"k":"v"}]
     */
    ARRAY_OBJECT,
    ;
    
    //容器类型
    private static List<NodeType> CONTAINER_TYPE = Arrays.asList(OBJECT, ARRAY_PRIMITIVE, ARRAY_OBJECT);
    
    public boolean isContainerType() {
        return CONTAINER_TYPE.contains(this);
    }
}
