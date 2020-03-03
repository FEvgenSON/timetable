package com.fevgenson.timetable.time

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.AttributeSet
import com.fevgenson.timetable.R

class CustomTextClock(context: Context?, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatTextView(context, attrs) {
    var time: String = ""
        set(time) {
            field = time
            update()
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
                    update()
                }
            }
        }
    }
    var day = -1
        set(day) {
            field = day
            update()
        }
    var weekType = -1
        set(weekType) {
            field = weekType
            update()
        }
    private var currentDay = false

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        context.registerReceiver(timeAndDateChangeReceiver, observeFilter)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        context.unregisterReceiver(timeAndDateChangeReceiver)
    }

    private fun updateCurrentDay() {
        currentDay = TimeChecker.currentDay == day && TimeChecker.currentWeekType == weekType
    }

    private fun updateTime() {
        if (!currentDay || time == "") {
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

    private fun update() {
        updateCurrentDay()
        updateTime()
    }
}