package com.lfjmgs.networkutils.net;

import android.content.Context;
import android.widget.Toast;


import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import retrofit2.HttpException;


/**
 * 错误处理，可在此对某些错误进行统一处理
 * Created by liufang03 on 2017/7/18.
 */

public class BaseErrorConsumer implements Consumer<Throwable> {
    private WeakReference<Context> mContextRef;

    public BaseErrorConsumer(Context context) {
        mContextRef = new WeakReference<>(context);
    }

    @Override
    public void accept(@NonNull Throwable e) throws Exception {
        onError(e);
    }

    /**
     * 错误处理，子类可重写此方法特殊处理
     *
     * @param e
     */
    protected void onError(@NonNull Throwable e) {
        e.printStackTrace();
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            onApiError(apiException);
        } else if (e instanceof SocketTimeoutException) {
            showToast("网络连接超时");
        } else if (e instanceof ConnectException) {
            showToast("网络连接异常");
        } else if (e instanceof HttpException) {
            showToast("网络错误");
        } else {
            showToast("未知错误");
        }
    }


    private void showToast(String msg) {
        Context context = mContextRef.get();
        if (context != null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 接口定义的错误处理，子类可重写此方法特殊处理
     *
     * @param apiException
     */
    protected void onApiError(ApiException apiException) {
        showToast(apiException.msg);
    }
}
