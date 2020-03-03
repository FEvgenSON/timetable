package com.fevgenson.timetable.time

import android.content.Context
import android.util.AttributeSet
import com.fevgenson.timetable.R
import io.reactivex.disposables.CompositeDisposable

class CustomTextClock(context: Context?, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatTextView(context, attrs) {
    private val disposable = CompositeDisposable()
    var time: String = ""
        set(time) {
            field = time
            update()
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
        disposable.add(TimeObserver.minuteObservable.subscribe { updateTime() })
        disposable.add(TimeObserver.dayObservable.subscribe { update() })
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposable.dispose()
        disposable.clear()
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