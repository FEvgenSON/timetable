package com.fevgenson.timetable.time

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.AttributeSet
import android.widget.TextView
import com.fevgenson.timetable.R

class CustomTextClock(context: Context?, attrs: AttributeSet?) : TextView(context, attrs) {
    var time: String = ""
        set(time) {
            field = time
            context.registerReceiver(timeAndDateChangeReceiver, observeFilter)
            updateCurrentDay()
            updateTime()
        }
    private val observeFilter = IntentFilter()

    init {
        observeFilter.addAction(Intent.ACTION_TIME_TICK)
        observeFilter.addAction(Intent.ACTION_DATE_CHANGED)
    }

    private val timeAndDateChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action.toString()) {
                Intent.ACTION_TIME_TICK -> updateTime()
                Intent.ACTION_DATE_CHANGED -> {
                    TimeChecker.init()
                    updateCurrentDay()
                    updateTime()
                }
            }
        }
    }
    var day = -1
        set(day) {
            field = day
            if (weekType != -1) {
                updateCurrentDay()
                if (time != "") {
                    updateTime()
                }
            }
        }
    var weekType = -1
        set(weekType) {
            field = weekType
            if (day != -1) {
                updateCurrentDay()
                if (time != "") {
                    updateTime()
                }
            }
        }
    private var currentDay = false

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        context.unregisterReceiver(timeAndDateChangeReceiver)
    }

    private fun updateCurrentDay() {
        currentDay = TimeChecker.currentDay == day && TimeChecker.currentWeekType == weekType
    }

    private fun updateTime() {
        if (!currentDay) {
            text = null
            return
        }
        val currentTime = TimeChecker.getCurrentTime()
        if (currentTime in
            TimeChecker.getFirstTime(time)..TimeChecker.getSecondTime(time)
        ) {
            text = context.getString(
                R.string.before_end, TimeChecker.minus(TimeChecker.getSecondTime(time), currentTime)
            )
            return
        }
        if (currentTime < TimeChecker.getFirstTime(time)) {
            text = context.getString(
                R.string.before_start,
                TimeChecker.minus(TimeChecker.getFirstTime(time), currentTime)
            )
            return
        }
        text = null
    }
}