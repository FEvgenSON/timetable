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
}