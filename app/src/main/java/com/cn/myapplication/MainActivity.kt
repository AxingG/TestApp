package com.cn.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : RxAppCompatActivity() {

    private val runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        initListener()
    }

    private fun initData() {
        //        ExecutorUtil.init();
        //        sendData(1);
    }

    private fun initListener() {
//        mTvJd!!.setOnClickListener { startActivity(Intent(this@MainActivity, JumpActivity::class.java)) }
        mTvJd!!.setOnClickListener { startActivity(Intent(this@MainActivity, NoSelectActivity::class.java)) }
    }

    private fun sendData(num: Int) {
        Observable.create(ObservableOnSubscribe<Int> { emitter ->
            for (i in 0..29999) {
                try {
                    Thread.sleep(200)
                } catch (e: Exception) {
                }

                emitter.onNext(i)
            }
        })
                .compose(ExecutorUtil.applyGlobalSchedulers(getLifecycleTransformer<Any>()))
                .subscribe { o ->
                    if (o is Int) {
                        Log.e("TAG", o.toString() + "")
                    }
                }.isDisposed
    }

    private fun <T> getLifecycleTransformer(): LifecycleTransformer<T> {
        return bindUntilEvent(ActivityEvent.DESTROY)
    }
}