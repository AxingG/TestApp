package com.cn.myapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class JumpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jump)
        for (i in 0..299) {
            sendData(i)
        }
    }

    private fun sendData(num: Int) {

    }
}
