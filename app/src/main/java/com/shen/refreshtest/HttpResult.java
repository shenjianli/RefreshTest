package com.shen.refreshtest;

public class HttpResult<T> {


    public static final int REQ_SUCC = 0xbb12;
    public static final int REQ_FAIL = REQ_SUCC + 1;
    public static final int REQ_EXCE = REQ_FAIL + 1;

    private int reqCode;
    private String msg;
    private T data;

    public int getReqCode() {
        return reqCode;
    }

    public void setReqCode(int reqCode) {
        this.reqCode = reqCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
