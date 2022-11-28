package com.demo.exception;

public class TemplateException extends RuntimeException {
    
    
    private static final long serialVersionUID = 296478906900416366L;
    
    /**
     * 错误编码
     */
    private Integer code;
    
    public TemplateException() {
        super();
    }
    
    public TemplateException(String message) {
        super(message);
        this.code = null;
    }
    
    public TemplateException(int code, String message) {
        super(message);
        this.code = code;
    }
    
    public TemplateException(ExceptionEnum exception) {
        super(exception.getMsg());
        this.code = exception.getCode();
    }
    
    public Integer getCode() {
        return code;
    }
}
