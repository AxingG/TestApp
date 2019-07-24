package com.cn.myapplication

import android.os.Bundle
import android.os.PersistableBundle
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * @author: zhaojiaxing@gmail.com
 * @created on: 2019/7/24 16:08
 * @description:
 */
class NoSelectActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.act_no_select)
        selectType(0)
        selectType(1)
        selectType(2)
    }

    private fun selectType(type: Int) {

    }
}