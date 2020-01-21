package com.fevgenson.timetable.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fevgenson.timetable.room.DBHolder
import com.fevgenson.timetable.room.entity.Lesson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class DayViewModel(private val weekType: Int, private val day: Int) : ViewModel() {
    val lessons = MutableLiveData<List<Lesson>>()
    private val disposable = CompositeDisposable()

    init {
        disposable.add(DBHolder.database.lessonDao().getLessons(weekType, day)
            .observeOn(AndroidSchedulers.mainThread()).subscribe { lessons.value = it })
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}