package com.ricky.pm.model;

/**
 * Created by liqi on 16/9/29.
 */
public class CommonInfo {

    public static final int CODE_SUCCESS =200;
    public static final int CODE_NOT_SYNC =400;
    public static final int CODE_ERROR =500;



    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
