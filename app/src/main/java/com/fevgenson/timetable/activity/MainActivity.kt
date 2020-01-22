package com.fevgenson.timetable.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fevgenson.timetable.R
import com.fevgenson.timetable.fragment.TimetableFragment
import com.fevgenson.timetable.room.DBHolder
import com.fevgenson.timetable.time.TimeChecker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //init
        DBHolder.init(applicationContext)
        TimeChecker.init()
        //action bar
        setSupportActionBar(mainActivityToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        //load default fragment
        if (savedInstanceState == null) {
            loadFragment(TimetableFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.mainActivityFragment, fragment)
            .commit()
    }
}
