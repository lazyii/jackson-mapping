package com.demo.jackson;

public class JsonViews {
    
    /**
     * 通用 简单view
     */
    public interface SimpleView {};
    
    /**
     * 通用 含明细view
     */
    public interface DetailView extends SimpleView {};

}
