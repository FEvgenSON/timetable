package com.fevgenson.timetable.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fevgenson.timetable.fragment.ListFragment
import com.fevgenson.timetable.room.DBHolder
import com.fevgenson.timetable.room.entity.*
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ListViewModel(val type: Int) : ViewModel() {
    val data = MutableLiveData<List<ListWithLessons>>()
    var expandableItems = mutableListOf<Int>()

    init {
        when (type) {
            ListFragment.NAME -> DBHolder.database.lessonDao().getNameWithLessons()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    data.value = it.map { withLessons -> ListWithLessons.getListFrom(withLessons) }
                }
            ListFragment.TEACHER -> DBHolder.database.lessonDao().getTeacherWithLessons()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    data.value = it.map { withLessons -> ListWithLessons.getListFrom(withLessons) }
                }
            ListFragment.BUILDING -> DBHolder.database.lessonDao().getBuildingWithLessons()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    data.value = it.map { withLessons -> ListWithLessons.getListFrom(withLessons) }
                }
            ListFragment.CLASSROOM -> DBHolder.database.lessonDao().getClassroomWithLessons()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    data.value = it.map { withLessons -> ListWithLessons.getListFrom(withLessons) }
                }
            ListFragment.TYPE -> DBHolder.database.lessonDao().getTypeWithLessons()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    data.value = it.map { withLessons -> ListWithLessons.getListFrom(withLessons) }
                }
            ListFragment.TIME -> DBHolder.database.lessonDao().getTimeWithLessons()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    data.value = it.map { withLessons -> ListWithLessons.getListFrom(withLessons) }
                }
        }
    }

    fun delete(position: Int) {
        val id = data.value!![position].id
        val name = data.value!![position].list
        Completable.fromRunnable {
            when (type) {
                ListFragment.NAME -> DBHolder.database.lessonDao().deleteName(Name(id, name))
                ListFragment.TEACHER -> DBHolder.database.lessonDao()
                    .deleteTeacher(Teacher(id, name))
                ListFragment.BUILDING -> DBHolder.database.lessonDao()
                    .deleteBuilding(Building(id, name))
                ListFragment.CLASSROOM -> DBHolder.database.lessonDao()
                    .deleteClassroom(Classroom(id, name))
                ListFragment.TYPE -> DBHolder.database.lessonDao().deleteType(Type(id, name))
                ListFragment.TIME -> DBHolder.database.lessonDao().deleteTime(Time(id, name))
            }
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun edit(position: Int, newValue: String): Boolean {
        data.value?.forEach { if (it.list == newValue) return false }
        val id = data.value!![position].id
        Completable.fromRunnable {
            when (type) {
                ListFragment.NAME -> DBHolder.database.lessonDao().updateName(Name(id, newValue))
                ListFragment.TEACHER -> DBHolder.database.lessonDao()
                    .updateTeacher(Teacher(id, newValue))
                ListFragment.BUILDING -> DBHolder.database.lessonDao()
                    .updateBuilding(Building(id, newValue))
                ListFragment.CLASSROOM -> DBHolder.database.lessonDao()
                    .updateClassroom(Classroom(id, newValue))
                ListFragment.TYPE -> DBHolder.database.lessonDao().updateType(Type(id, newValue))
                ListFragment.TIME -> DBHolder.database.lessonDao().updateTime(Time(id, newValue))
            }
        }.subscribeOn(Schedulers.io()).subscribe()
        return true
    }

    fun add(newValue: String): Boolean {
        data.value?.forEach { if (it.list == newValue) return false }
        Completable.fromRunnable {
            when (type) {
                ListFragment.NAME -> DBHolder.database.lessonDao().insertName(Name(name = newValue))
                ListFragment.TEACHER -> DBHolder.database.lessonDao()
                    .insertTeacher(Teacher(name = newValue))
                ListFragment.BUILDING -> DBHolder.database.lessonDao()
                    .insertBuilding(Building(name = newValue))
                ListFragment.CLASSROOM -> DBHolder.database.lessonDao()
                    .insertClassroom(Classroom(name = newValue))
                ListFragment.TYPE -> DBHolder.database.lessonDao().insertType(Type(name = newValue))
                ListFragment.TIME -> DBHolder.database.lessonDao().insertTime(Time(name = newValue))
            }
        }.subscribeOn(Schedulers.io()).subscribe()
        return true
    }
}