package com.fevgenson.timetable.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fevgenson.timetable.R
import com.fevgenson.timetable.fragment.DictionaryFragment
import com.fevgenson.timetable.fragment.TimetableFragment
import com.fevgenson.timetable.room.DBHolder
import com.fevgenson.timetable.service.NotificationService
import com.fevgenson.timetable.time.TimeChecker
import com.fevgenson.timetable.time.TimeObserver
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var activeTag = TIMETABLE

    companion object {
        private const val TIMETABLE = "tt"
        private const val DICTIONARY = "dt"
        private const val SETTINGS = "st"
        private const val ACTIVE_TAG = "at"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //init
        TimeObserver.init(applicationContext)
        DBHolder.init(applicationContext)
        TimeChecker.init()
        //action bar
        setSupportActionBar(mainActivityToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        mainActivityBottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigationTimetable -> loadFragment(TIMETABLE)
                R.id.navigationDictionary -> loadFragment(DICTIONARY)
            }
            return@setOnNavigationItemSelectedListener true
        }
        //load default fragment
        activeTag = if (savedInstanceState == null) {
            startService(Intent(this, NotificationService::class.java))
            loadFragment(TIMETABLE)
            TIMETABLE
        } else {
            savedInstanceState.getString(ACTIVE_TAG, TIMETABLE)
        }
    }

    private fun loadFragment(tag: String) {
        val activeFragment = supportFragmentManager.findFragmentByTag(activeTag)
        var fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = when (tag) {
                TIMETABLE -> TimetableFragment()
                DICTIONARY -> DictionaryFragment()
                else -> throw IllegalArgumentException("wrong tag")
            }
            if (activeFragment != null) {
                supportFragmentManager.beginTransaction().hide(activeFragment).commit()
            }
            supportFragmentManager.beginTransaction().add(R.id.mainActivityFragment, fragment, tag)
                .commit()
        } else {
            supportFragmentManager.beginTransaction().hide(activeFragment!!).show(fragment)
                .commit()
        }
        activeTag = tag
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ACTIVE_TAG, activeTag)
    }
}
