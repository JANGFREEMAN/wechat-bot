package com.wechat.exception;

/**
 * 微信端异常类
 */
public class WeChatException extends Exception{

    private String code ;

    private String msg;


    public WeChatException(String errMsg){
        super(errMsg);
    }

    public WeChatException(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
