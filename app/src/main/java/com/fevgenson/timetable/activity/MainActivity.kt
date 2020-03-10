package com.fevgenson.timetable.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import com.fevgenson.timetable.R
import com.fevgenson.timetable.databinding.ActivityMainBinding
import com.fevgenson.timetable.navigation.BottomSaveStateNavigator
import com.fevgenson.timetable.room.DBHolder
import com.fevgenson.timetable.time.TimeChecker
import com.fevgenson.timetable.time.TimeObserver
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //init
        TimeObserver.init(applicationContext)
        DBHolder.init(applicationContext)
        TimeChecker.init()
        //action bar
        setSupportActionBar(mainActivityToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        //navigation
        val navController = findNavController(R.id.mainActivityFragment)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainActivityFragment)!!
        val navigator = BottomSaveStateNavigator(
            this,
            navHostFragment.childFragmentManager,
            R.id.mainActivityFragment
        )
        navController.navigatorProvider += navigator
        navController.setGraph(R.navigation.bottom_navigation)
        binding.mainActivityBottomNavigation.setupWithNavController(navController)
    }
}
