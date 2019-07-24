package com.cn.myapplication

import android.os.Bundle
import android.util.Log

import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.functions.Consumer

class JumpActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jump)
        for (i in 0..299) {
            sendData(i)
        }
    }

    private fun sendData(num: Int) {
        Observable.create(ObservableOnSubscribe<Int> { emitter ->
            try {
                Thread.sleep(200)
            } catch (e: Exception) {
            }

            emitter.onNext(num)
        })
                .compose(ExecutorServices.applyGlobalSchedulers(getLifecycleTransformer<Any>()))
                .subscribe { o ->
                    if (o is Int) {
                        Log.e("TAG", o.toString() + "_lalala")
                    }
                }.isDisposed
    }

    private fun <T> getLifecycleTransformer(): LifecycleTransformer<T> {
        return bindUntilEvent(ActivityEvent.DESTROY)
    }
}
