package com.lfjmgs.networkutils.net;

import java.io.IOException;

/**
 * Created by liufang03 on 2017/7/18.
 */
public class ApiException extends IOException {
    public final String code;
    public final String msg;

    public ApiException(String code) {
        this.code = code;
        this.msg = null;
    }

    public ApiException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
