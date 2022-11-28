package com.demo;

import lombok.Data;

@Data
public class Mapping {
    
    private String targetUrlId;
    
    private int targetNodeId;
    
    private String sourceUrlId;
    
    private int sourceNodeId;
}
