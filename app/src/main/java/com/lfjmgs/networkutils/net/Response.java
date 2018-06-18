package com.lfjmgs.networkutils.net;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liufang03 on 2017/1/13.
 */
public class Response implements Serializable {

    public static final String REQUEST_SUCCESS = "000"; // 请求成功的状态码
    public static final String KEY_CODE = "status"; // 状态码
    public static final String KEY_MESSAGE = "message"; // 错误消息
    public static final String KEY_DATA = "data";

    @SerializedName(KEY_CODE)
    public String resCode;
    @SerializedName(KEY_MESSAGE)
    public String resMsg;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public boolean isSuccessful() {
        return REQUEST_SUCCESS.equals(resCode);
    }
}
