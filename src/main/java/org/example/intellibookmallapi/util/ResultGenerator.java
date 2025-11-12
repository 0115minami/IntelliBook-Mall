package org.example.intellibookmallapi.util;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {
    
    private static final int SUCCESS_CODE = 200;
    private static final int FAIL_CODE = 500;
    private static final String SUCCESS_MESSAGE = "success";
    
    /**
     * 成功响应（无数据）
     */
    public static Result genSuccessResult() {
        return new Result(SUCCESS_CODE, SUCCESS_MESSAGE);
    }
    
    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> genSuccessResult(T data) {
        return new Result<>(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }
    
    /**
     * 成功响应（自定义消息）
     */
    public static Result genSuccessResult(String message) {
        return new Result(SUCCESS_CODE, message);
    }
    
    /**
     * 失败响应
     */
    public static Result genFailResult(String message) {
        return new Result(FAIL_CODE, message);
    }
    
    /**
     * 失败响应（自定义错误码）
     */
    public static Result genFailResult(int code, String message) {
        return new Result(code, message);
    }
}
