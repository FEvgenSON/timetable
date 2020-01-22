package com.fevgenson.timetable.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fevgenson.timetable.room.DBHolder
import com.fevgenson.timetable.room.entity.Lesson
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DayViewModel(val weekType: Int, val day: Int) : ViewModel() {
    val lessons = MutableLiveData<List<Lesson>>()
    var expandedItemsId = mutableListOf<Int>()
    private val disposable = CompositeDisposable()

    init {
        disposable.add(DBHolder.database.lessonDao().getLessons(weekType, day)
            .observeOn(AndroidSchedulers.mainThread()).subscribe { lessons.value = it })
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    fun delete(position: Int) {
        Completable.fromRunnable {
            DBHolder.database.lessonDao().deleteLesson(lessons.value!![position])
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun copyDialogResult(weekType: Int, day: Int, position: Int) {
        val copyLesson = lessons.value!![position].copy(id = 0, weekType = weekType, day = day)
        Completable.fromRunnable {
            DBHolder.database.lessonDao().insertLesson(copyLesson)
        }.subscribeOn(Schedulers.io()).subscribe()
    }
}