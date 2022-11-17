package com.example.shoppinglistapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.ScrollingActivity


class SplashActivity : Activity() {
    var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        handler = Handler()
        handler!!.postDelayed({
            val intent = Intent(this@SplashActivity, ScrollingActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}