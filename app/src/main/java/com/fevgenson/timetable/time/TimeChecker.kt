package com.fevgenson.timetable.time

import java.text.SimpleDateFormat
import java.util.*

object TimeChecker {
    var currentDay: Int = 0 //init later
        private set
    var currentWeekType = 0 //init later
        private set

    lateinit var dates: MutableList<MutableList<String>>

    fun init() {
        initCurrentDay()
        initWeekType()
        initDates()
    }

    private fun initDates() {
        dates = mutableListOf()
        dates.add(mutableListOf())
        dates.add(mutableListOf())
        val simpleDateFormat = SimpleDateFormat("dd:MM", Locale.US)
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        for (i in 0..6) {
            calendar.set(Calendar.DAY_OF_WEEK, if (i == 6) Calendar.SUNDAY else i + 2)
            dates[currentWeekType].add(simpleDateFormat.format(calendar.time))
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
            dates[if (currentWeekType == 0) 1 else 0].add(simpleDateFormat.format(calendar.time))
            calendar.add(Calendar.WEEK_OF_YEAR, -1)
        }
    }

    private fun initCurrentDay() {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        currentDay = calendar.get(Calendar.DAY_OF_WEEK)
        currentDay = if (currentDay == Calendar.SUNDAY) 6 else currentDay - 2
    }

    private fun initWeekType() {
        val now = Date()
        val calendar = Calendar.getInstance()
        calendar.time = now
        val startYear =
            if (calendar.get(Calendar.MONTH) >= 8) calendar.get(Calendar.YEAR)
            else calendar.get(Calendar.YEAR) - 1
        calendar.set(startYear, 8, 1)
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.set(startYear, 8, 2)
        }
        val firstDay = calendar.time
        currentWeekType = if ((((now.time - firstDay.time) / (1000 * 60 * 60 * 24)
                    + calendar.get(Calendar.DAY_OF_WEEK) - 2) / 7).toInt() % 2 == 0
        ) 0 else 1
    }

    fun getCurrentTime(): String {
        val now = Date()
        val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.US)
        return simpleDateFormat.format(now)
    }

    fun getFirstTime(timeRange: String): String {
        return timeRange.substring(0, 5)
    }

    fun getSecondTime(timeRange: String): String {
        return timeRange.substring(6)
    }

    fun getHours(time: String): String {
        return time.substring(0, 2)
    }

    fun getMinutes(time: String): String {
        return time.substring(3)
    }

    fun getCurrentHour(): Int {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun getCurrentMinutes(): Int {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        return calendar.get(Calendar.MINUTE)
    }
}