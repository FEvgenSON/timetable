package com.fevgenson.timetable.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fevgenson.timetable.R
import com.fevgenson.timetable.room.DBHolder
import com.fevgenson.timetable.room.entity.*
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CreateViewModel(var weekType: Int, var day: Int, private val id: Int) :
    ViewModel() {
    val lesson = MutableLiveData<Lesson>()
    val names = MutableLiveData<List<Name>>()
    val teachers = MutableLiveData<List<Teacher>>()
    val buildings = MutableLiveData<List<Building>>()
    val classrooms = MutableLiveData<List<Classroom>>()
    val types = MutableLiveData<List<Type>>()
    val times = MutableLiveData<List<Time>>()
    val toastError = MutableLiveData<Int>()
    val nameError = MutableLiveData<Boolean>()
    val finish = MutableLiveData<Boolean>()
    private val disposable = CompositeDisposable()

    init {
        val dao = DBHolder.database.lessonDao()
        if (id == -1) {
            lesson.value = Lesson(weekType = weekType, day = day)
        } else {
            disposable.add(
                dao.getLesson(weekType, day, id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ lesson.value = it }, { it.printStackTrace() })
            )
        }
        disposable.add(
            dao.getAllName().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ names.value = it }, {})
        )
        disposable.add(
            dao.getAllTeacher().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ teachers.value = it }, {})
        )
        disposable.add(
            dao.getAllBuilding().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ buildings.value = it }, {})
        )
        disposable.add(
            dao.getAllClassroom().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ classrooms.value = it }, {})
        )
        disposable.add(
            dao.getAllType().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ types.value = it }, {})
        )
        disposable.add(
            dao.getAllTime().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ times.value = it }, {})
        )
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
            nameError.value = true
            return
        }
        if (!validateTime(startTime) || !validateTime(endTime)) {
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
        savableLesson.teacher = if (teacher.isBlank()) "" else teacher
        savableLesson.building = if (building.isBlank()) "" else building
        savableLesson.classroom = if (classroom.isBlank()) "" else classroom
        savableLesson.type = if (type.isBlank()) "" else type
        savableLesson.time = "$startTime-$endTime"
        savableLesson.weekType = weekType
        savableLesson.day = day
        if (id == -1) {
            Completable.fromRunnable {
                DBHolder.database.lessonDao().insertLessonAndColumns(savableLesson)
            }.subscribeOn(Schedulers.io()).subscribe()
        } else {
            Completable.fromRunnable {
                DBHolder.database.lessonDao().updateLessonAndColumns(savableLesson)
            }.subscribeOn(Schedulers.io()).subscribe()
        }
        finish.value = true
    }

    fun validateTime(time: String): Boolean {
        return time[0] == '0' || time[0] == '1' || time[0] == '2'
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}