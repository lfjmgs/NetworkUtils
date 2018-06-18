package com.lfjmgs.networkutils.net;

/**
 * Created by liufang03 on 2017/8/17.
 */

public class DataIsNullException extends ApiException {
    public DataIsNullException() {
        super("-1", "data字段为null");
    }
}
