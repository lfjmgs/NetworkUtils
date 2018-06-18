package com.lfjmgs.networkutils.net;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 给网络请求加loading
 * Created by liufang03 on 2017/7/18.
 */

public class LoadingTransformer<T> implements ObservableTransformer<T, T> {
    private LoadingCallback mLoadingCallback;
    private boolean mShowLoading;

    public LoadingTransformer(LoadingCallback loadingCallback) {
        mLoadingCallback = loadingCallback;
        mShowLoading = true;
    }

    public LoadingTransformer(LoadingCallback loadingCallback, boolean showLoading) {
        mLoadingCallback = loadingCallback;
        mShowLoading = showLoading;
    }

    @Override
    public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
        if (!mShowLoading) {
            return upstream;
        }
        return upstream.doOnSubscribe(mLoadingCallback::showLoading)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doFinally(mLoadingCallback::dismissLoading)
                .unsubscribeOn(AndroidSchedulers.mainThread());
    }


    public interface LoadingCallback {
        void showLoading(Disposable disposable);
        void dismissLoading();
    }
}
