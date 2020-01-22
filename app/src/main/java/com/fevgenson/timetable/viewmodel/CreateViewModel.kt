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

class CreateViewModel(var weekType: Int, var day: Int, private val position: Int) :
    ViewModel() {
    val lesson = MutableLiveData<Lesson>()
    val names = MutableLiveData<List<Name>>()
    val teachers = MutableLiveData<List<Teacher>>()
    val buildings = MutableLiveData<List<Building>>()
    val classrooms = MutableLiveData<List<Classroom>>()
    val types = MutableLiveData<List<Type>>()
    val times = MutableLiveData<List<Time>>()
    val toastError = MutableLiveData<Int>()
    val layoutError = MutableLiveData<Int>()
    val finish = MutableLiveData<Boolean>()
    private val disposable = CompositeDisposable()

    init {
        val dao = DBHolder.database.lessonDao()
        if (position == -1) {
            lesson.value = Lesson(weekType = weekType, day = day)
        } else {
            disposable.add(
                dao.getLessons(weekType, day)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ lesson.value = it[position] }, { /*fix strange error*/ })
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
        savableLesson.teacher = teacher
        savableLesson.building = building
        savableLesson.classroom = classroom
        savableLesson.type = type
        savableLesson.time = "$startTime-$endTime"
        savableLesson.weekType = weekType
        savableLesson.day = day
        if (position == -1) {
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