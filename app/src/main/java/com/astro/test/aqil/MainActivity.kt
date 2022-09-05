package com.astro.test.aqil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.astro.R


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.setCurrentActivity(this)
        supportActionBar?.title = ""
    }
}