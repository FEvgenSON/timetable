package com.fevgenson.timetable.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fevgenson.timetable.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //action bar
        setSupportActionBar(mainActivityToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
}
