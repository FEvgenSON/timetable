package com.fevgenson.timetable.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fevgenson.timetable.fragment.ListFragment
import com.fevgenson.timetable.room.DBHolder
import com.fevgenson.timetable.room.entity.ListWithLessons
import io.reactivex.android.schedulers.AndroidSchedulers

class ListViewModel(type: Int) : ViewModel() {
    val data = MutableLiveData<List<ListWithLessons>>()

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
}