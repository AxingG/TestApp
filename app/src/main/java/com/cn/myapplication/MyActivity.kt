package com.cn.myapplication

import android.arch.lifecycle.LifecycleOwner
import android.support.v7.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @author: zhaojiaxing@gmail.com
 * @created on: 2019/7/31 11:44
 * @description:
 */
open class MyActivity : AppCompatActivity() {

    fun <T> GlobalScope.asyncWithLifecycle(lifecycleOwner: LifecycleOwner,
                                           context: CoroutineContext = EmptyCoroutineContext,
                                           start: CoroutineStart = CoroutineStart.DEFAULT,
                                           block: suspend CoroutineScope.() -> T): Deferred<T> {
        val deferred = GlobalScope.async(context, start) {
            block()
        }
        lifecycleOwner.lifecycle.addObserver(LifecycleCoroutineListener(deferred))
        return deferred
    }
}