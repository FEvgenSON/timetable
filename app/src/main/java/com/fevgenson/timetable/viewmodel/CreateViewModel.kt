package com.fevgenson.timetable.viewmodel

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fevgenson.timetable.R
import com.fevgenson.timetable.room.DBHolder
import com.fevgenson.timetable.room.entity.Lesson
import io.reactivex.disposables.Disposable

class CreateViewModel(weekType: Int, day: Int, private val position: Int) :
    ViewModel() {
    val lesson = MutableLiveData<Lesson>()
    val toastError = MutableLiveData<Int>()
    val layoutError = MutableLiveData<Int>()
    val finish = MutableLiveData<Boolean>()
    private var disposable: Disposable? = null

    init {
        if (position == -1) {
            lesson.value = Lesson(weekType = weekType, day = day)
        } else {
            disposable = DBHolder.database.lessonDao().getLessons(weekType, day)
                .subscribe {
                    lesson.value = it[position]
                    disposable?.dispose()
                }
        }
    }

    fun save(
        name: String,
        teacher: String,
        building: String,
        classroom: String,
        type: String,
        startTime: String,
        endTime: String
    ) {
        //validate
        if (name.isBlank()) {
            layoutError.value = R.id.nameLayout
            return
        }
        if (teacher.isBlank()) {
            layoutError.value = R.id.teacherLayout
            return
        }
        if (building.isBlank()) {
            layoutError.value = R.id.buildingLayout
            return
        }
        if (classroom.isBlank()) {
            layoutError.value = R.id.classroomLayout
            return
        }
        if (type.isBlank()) {
            layoutError.value = R.id.typeLayout
            return
        }
        if (!startTime.isDigitsOnly() || !endTime.isDigitsOnly()) {
            toastError.value = R.string.empty_time_error
            return
        }
        if (startTime >= endTime) {
            toastError.value = R.string.value_time_error
            return
        }
        //save
        val savableLesson = lesson.value!!.copy()
        savableLesson.name = name
        savableLesson.teacher = teacher
        savableLesson.building = building
        savableLesson.classroom = classroom
        savableLesson.type = type
        savableLesson.time = "$startTime-$endTime"
        if (position == -1) {
            DBHolder.database.lessonDao().insertLessonAndColumns(savableLesson)
        } else {
            DBHolder.database.lessonDao().updateLessonAndColumns(savableLesson)
        }
        finish.value = true
    }

    override fun onCleared() {
        super.onCleared()
        disposable = null
    }
}