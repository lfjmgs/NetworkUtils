package com.lfjmgs.networkutils.net;

import android.support.annotation.NonNull;

import java.io.IOException;


import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liufang03 on 2017/7/17.
 */

public class CommonHeadersInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder()
                .header("Charset", "UTF-8")
                // 不能自己添加，否则不会自动解压
                // .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "zh-CN, zh;q=0.8, en-US;q=0.5, en;q=0.3")
                .method(original.method(), original.body());

        Response response = chain.proceed(builder.build());

        return response;
    }
}
