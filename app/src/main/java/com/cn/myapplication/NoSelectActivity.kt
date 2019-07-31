package com.cn.myapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.coroutines.*

/**
 * @author: zhaojiaxing@gmail.com
 * @created on: 2019/7/24 16:08
 * @description:
 */
class NoSelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_no_select)
        test1()
        test2()
    }

    private fun test1() {
        println("test1主线程：Thread_id = " + Thread.currentThread().id)
        GlobalScope.launch {
            println("test1启动一个协程：Thread_id = " + Thread.currentThread().id)
            delay(3000)
            val s = "我是协程内字符串"
            withContext(Dispatchers.Main) {
                println("test1主线程：Thread_id = " + Thread.currentThread().id + " $s")
            }
        }
        println("test1方法执行完毕")
    }

    private fun test2() {
        println("test2主线程：Thread_id = " + Thread.currentThread().id)
        GlobalScope.launch(Dispatchers.Main) {
            println("test2启动一个协程：Thread_id = " + Thread.currentThread().id)
            val result = myTask()
            println("test2异步执行结果：$result")
            println("test2Launch完毕")
        }
        println("test2方法执行完毕")
    }

    private suspend fun myTask(): String {
        return GlobalScope.async {
            delay(3000)
            println("test2协程内执行异步任务：Thread_id = " + Thread.currentThread().id)
            return@async "返回结果"
        }.await()
    }

    override fun onDestroy() {
        super.onDestroy()
        println("destroy()")
    }
}