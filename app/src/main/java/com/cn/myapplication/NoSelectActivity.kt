package com.cn.myapplication

import android.os.Bundle
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

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
    }

    private fun selectType(type: Int): String {
        return "(●￣(ｴ)￣●)"
    }
}