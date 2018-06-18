package com.lfjmgs.networkutils.net;

import android.content.Context;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.Functions;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by liufang03 on 2017/7/17.
 */

public class BaseApiObserver<T> extends AtomicReference<Disposable> implements Observer<T>,
        Disposable, CompletableObserver {

    private final Consumer<? super T> mOnNext;
    private final Consumer<? super Throwable> mOnError;
    private final Action mOnComplete;
    private final Consumer<? super Disposable> mOnSubscribe;

    public BaseApiObserver(Context context, Consumer<? super T> onNext) {
        this(onNext, new BaseErrorConsumer(context));
    }

    public BaseApiObserver(Context context, Action onComplete) {
        this(onComplete, new BaseErrorConsumer(context));
    }

    public BaseApiObserver(Consumer<? super T> onNext,
                           Consumer<? super Throwable> onError) {
        super();
        mOnNext = onNext;
        mOnError = onError;
        mOnComplete = Functions.EMPTY_ACTION;
        mOnSubscribe = Functions.emptyConsumer();
    }

    public BaseApiObserver(Action onComplete,
                           Consumer<? super Throwable> onError) {
        super();
        mOnNext = Functions.emptyConsumer();
        mOnError = onError;
        mOnComplete = onComplete;
        mOnSubscribe = Functions.emptyConsumer();
    }

    @Override
    public void onSubscribe(Disposable s) {
        if (DisposableHelper.setOnce(this, s)) {
            try {
                mOnSubscribe.accept(this);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                s.dispose();
                onError(ex);
            }
        }
    }

    @Override
    public void onNext(T t) {
        if (!isDisposed()) {
            try {
                mOnNext.accept(t);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                get().dispose();
                onError(e);
            }
        }
    }

    @Override
    public void onError(Throwable t) {
        if (!isDisposed()) {
            lazySet(DisposableHelper.DISPOSED);
            try {
                mOnError.accept(t);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(new CompositeException(t, e));
            }
        }
    }

    @Override
    public void onComplete() {
        if (!isDisposed()) {
            lazySet(DisposableHelper.DISPOSED);
            try {
                mOnComplete.run();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
        }
    }

    @Override
    public void dispose() {
        DisposableHelper.dispose(this);
    }

    @Override
    public boolean isDisposed() {
        return get() == DisposableHelper.DISPOSED;
    }
}
