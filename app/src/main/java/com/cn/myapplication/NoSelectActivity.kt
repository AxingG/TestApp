package com.cn.myapplication

import android.os.Bundle
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

/**
 * @author: zhaojiaxing@gmail.com
 * @created on: 2019/7/24 16:08
 * @description:
 */
class NoSelectActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_no_select)
        println(selectType(0))
        selectType(1)
        selectType(2)
        loadData()
        println("主干")
    }

    private fun selectType(type: Int): String {
        return "(●￣(ｴ)￣●)" + Thread.currentThread().id
    }

    private fun loadData() = launch(UI) {
        println("执行异步任务之前：Thread_id = " + Thread.currentThread().id)
        val result1 = async(CommonPool) {
            println("执行异步任务1：Thread_id = " + Thread.currentThread().id)
            return@async "我是任务1"
        }.await()
        val result2 = async(CommonPool) {
            println("执行异步任务2：Thread_id = " + Thread.currentThread().id)
            return@async "我是任务2"
        }.await()
        val result = "$result1 $result2" // ui thread
        println("执行异步任务之后：result = $result")
    }
}