package com.demo.exception;

public enum ExceptionEnum {
    CODE_1000(1000, "没有根节点，无法生成模板"),
    
    ;
    
    
    private int code;
    private String msg;
    
    ExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getMsg() {
        return msg;
    }
}
