package com.fevgenson.timetable.room.dao

import androidx.room.*
import com.fevgenson.timetable.room.entity.*
import io.reactivex.Flowable

@Dao
abstract class LessonDao {

    @Query("SELECT * FROM lesson WHERE weekType == :weekType AND day == :day")
    abstract fun getLessons(weekType: Int, day: Int): Flowable<List<Lesson>>

    @Query("SELECT name from name")
    abstract fun getNameWithLessons(): Flowable<List<NameWithLessons>>

    @Query("SELECT name from teacher")
    abstract fun getTeacherWithLessons(): Flowable<List<TeacherWithLessons>>

    @Query("SELECT name from building")
    abstract fun getBuildingWithLessons(): Flowable<List<BuildingWithLessons>>

    @Query("SELECT name from classroom")
    abstract fun getClassroomWithLessons(): Flowable<List<ClassroomWithLessons>>

    @Query("SELECT name from type")
    abstract fun getTypeWithLessons(): Flowable<List<TypeWithLessons>>

    @Query("SELECT name from time")
    abstract fun getTimeWithLessons(): Flowable<List<TimeWithLessons>>

    @Transaction
    open fun insertLessonAndColumns(lesson: Lesson) {
        insertName(Name(lesson.name))
        insertTeacher(Teacher(lesson.teacher))
        insertBuilding(Building(lesson.building))
        insertClassroom(Classroom(lesson.classroom))
        insertType(Type(lesson.type))
        insertTime(Time(lesson.time))
        insertLesson(lesson)
    }

    @Transaction
    open fun updateLessonAndColumns(lesson: Lesson) {
        insertName(Name(lesson.name))
        insertTeacher(Teacher(lesson.teacher))
        insertBuilding(Building(lesson.building))
        insertClassroom(Classroom(lesson.classroom))
        insertType(Type(lesson.type))
        insertTime(Time(lesson.time))
        updateLesson(lesson)
    }

    @Update
    abstract fun updateLesson(lesson: Lesson)

    @Insert
    abstract fun insertLesson(lesson: Lesson)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertName(name: Name)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertTeacher(teacher: Teacher)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertBuilding(building: Building)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertClassroom(classroom: Classroom)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertType(type: Type)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertTime(time: Time)

    @Delete
    abstract fun deleteLesson(lesson: Lesson)

    @Delete
    abstract fun deleteName(name: Name)

    @Delete
    abstract fun deleteTeacher(teacher: Teacher)

    @Delete
    abstract fun deleteBuilding(building: Building)

    @Delete
    abstract fun deleteClassroom(classroom: Classroom)

    @Delete
    abstract fun deleteType(type: Type)

    @Delete
    abstract fun deleteTime(time: Time)
}