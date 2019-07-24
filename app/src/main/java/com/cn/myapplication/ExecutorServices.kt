package com.cn.myapplication

import com.trello.rxlifecycle2.LifecycleTransformer

import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.schedulers.Schedulers

/**
 * @author: zhaojiaxing@gmail.com
 * @created on: 2019/7/16 10:43
 * @description:
 */
class ExecutorServices private constructor(diskIO: ExecutorService = diskIoExecutor()) {

    init {
        mDiskIO = diskIO
    }

    companion object {

        @Volatile
        private var mInstance: ExecutorServices? = null
        private var mDiskIO: ExecutorService

        fun init() {
            if (mInstance == null) {
                synchronized(ExecutorServices::class.java) {
                    if (mInstance == null) {
                        mInstance = ExecutorServices()
                    }
                }
            }
        }

        fun io(): Scheduler {
            return Schedulers.from(mDiskIO)
        }

        /** main线程  */
        @NonNull
        fun main(): Scheduler {
            return AndroidSchedulers.mainThread()
        }

        /** 生命周期调度 通用  */
        @NonNull
        fun <T> applyLifecycle(lifecycleTransformer: LifecycleTransformer<T>?): ObservableTransformer<T, T> {
            return ObservableTransformer { upstream ->
                if (lifecycleTransformer != null) {
                    upstream.compose(lifecycleTransformer)
                } else {
                    upstream
                }
            }
        }

        /** 线程调度 通用  */
        @NonNull
        fun <T> applySchedulers(): ObservableTransformer<T, T> {
            return ObservableTransformer { upstream ->
                upstream.subscribeOn(io())
                        .unsubscribeOn(io())
                        .observeOn(main())
            }
        }

        /** 整体调度 包含线程调度及生命周期调度  */
        @NonNull
        fun <T> applyGlobalSchedulers(lifecycleTransformer: LifecycleTransformer<T>): ObservableTransformer<T, T> {
            return ObservableTransformer { upstream ->
                upstream.compose(ExecutorServices.applySchedulers())
                        .compose(applyLifecycle(lifecycleTransformer))
            }
        }

        private fun diskIoExecutor(): ExecutorService {
            return ThreadPoolExecutor(5, 50, 60, TimeUnit.SECONDS,
                    LinkedBlockingQueue())
        }
    }
}