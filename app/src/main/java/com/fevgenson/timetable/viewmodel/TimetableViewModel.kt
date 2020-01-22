package com.fevgenson.timetable.viewmodel

import androidx.lifecycle.ViewModel
import com.fevgenson.timetable.time.TimeChecker

class TimetableViewModel : ViewModel() {
    var savedSelectedWeekType = TimeChecker.currentWeekType
    var savedSelectedDayType = TimeChecker.currentDay
}