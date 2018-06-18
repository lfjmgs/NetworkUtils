package com.lfjmgs.networkutils.net;


/**
 * Created by liufang03 on 2017/1/13.
 */

public class Result<T> extends Response {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
