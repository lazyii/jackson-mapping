package com.demo;

import com.demo.jackson.JsonViews.DetailView;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class MapNode {
    
    /**
     * 主键id
     */
    private Long id;
    
    /**
     * 节点id
     */
    private int nodeId;
    
    /**
     * 父节点id
     */
    private Integer parentNodeId;
    
    /**
     * 层级，根定义为：0. 例如：{"k":"v"}. k的level为1
     */
    private int level;
    
    /**
     * 字段名
     */
    private String fieldName;
    
    /**
     * 示例值
     */
    private String example;
    
    /**
     * 字段说明或描述
     */
    private String description;
    
    /**
     * 该字段是否必填.true:必填， false:非必填
     */
    private Boolean required;
    
    /**
     * 字段类型
     */
    private NodeType nodeType;
    
    @JsonView(value = DetailView.class)
    private JsonNode jsonNode;
}
