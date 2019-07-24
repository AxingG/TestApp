package com.cn.myapplication;

import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: zhaojiaxing@gmail.com
 * @created on: 2019/7/16 10:43
 * @description:
 */
public class ExecutorServices {

    private volatile static ExecutorServices mInstance;
    private static ExecutorService mDiskIO;

    public static void init() {
        if (mInstance == null) {
            synchronized (ExecutorServices.class) {
                if (mInstance == null) {
                    mInstance = new ExecutorServices();
                }
            }
        }
    }

    private ExecutorServices() {
        this(diskIoExecutor());
    }

    private ExecutorServices(ExecutorService diskIO) {
        mDiskIO = diskIO;
    }

    public static Scheduler io() {
        return Schedulers.from(mDiskIO);
    }

    /** main线程 */
    @NonNull
    public static Scheduler main() {
        return AndroidSchedulers.mainThread();
    }

    /** 生命周期调度 通用 */
    @NonNull
    public static <T> ObservableTransformer<T, T> applyLifecycle(final LifecycleTransformer<T> lifecycleTransformer) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                if (lifecycleTransformer != null) {
                    return upstream.compose(lifecycleTransformer);
                } else {
                    return upstream;
                }
            }
        };
    }

    /** 线程调度 通用 */
    @NonNull
    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(io())
                        .unsubscribeOn(io())
                        .observeOn(main());
            }
        };
    }

    /** 整体调度 包含线程调度及生命周期调度 */
    @NonNull
    public static <T> ObservableTransformer<T, T> applyGlobalSchedulers(final LifecycleTransformer<T> lifecycleTransformer) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.compose(ExecutorServices.<T>applySchedulers())
                        .compose(applyLifecycle(lifecycleTransformer));
            }
        };
    }

    private static ExecutorService diskIoExecutor() {
        return new ThreadPoolExecutor(5, 50, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
    }
}