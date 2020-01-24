package com.fevgenson.timetable.room.dao

import androidx.room.*
import com.fevgenson.timetable.room.entity.*
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
abstract class LessonDao {

    @Transaction
    @Query("SELECT * FROM lesson WHERE weekType == :weekType AND day == :day ORDER BY time")
    abstract fun getLessons(weekType: Int, day: Int): Flowable<List<Lesson>>

    @Transaction
    @Query("SELECT * FROM lesson WHERE weekType == :weekType AND day == :day AND id == :id")
    abstract fun getLesson(weekType: Int, day: Int, id: Int): Single<Lesson>

    @Transaction
    @Query("SELECT id, name from name")
    abstract fun getNameWithLessons(): Flowable<List<NameWithLessons>>

    @Query("SELECT * FROM name")
    abstract fun getAllName(): Single<List<Name>>

    @Transaction
    @Query("SELECT id, name from teacher")
    abstract fun getTeacherWithLessons(): Flowable<List<TeacherWithLessons>>

    @Query("SELECT * FROM teacher")
    abstract fun getAllTeacher(): Single<List<Teacher>>

    @Transaction
    @Query("SELECT id, name from building")
    abstract fun getBuildingWithLessons(): Flowable<List<BuildingWithLessons>>

    @Query("SELECT * FROM building")
    abstract fun getAllBuilding(): Single<List<Building>>

    @Transaction
    @Query("SELECT id, name from classroom")
    abstract fun getClassroomWithLessons(): Flowable<List<ClassroomWithLessons>>

    @Query("SELECT * FROM classroom")
    abstract fun getAllClassroom(): Single<List<Classroom>>

    @Transaction
    @Query("SELECT id, name from type")
    abstract fun getTypeWithLessons(): Flowable<List<TypeWithLessons>>

    @Query("SELECT * FROM type")
    abstract fun getAllType(): Single<List<Type>>

    @Transaction
    @Query("SELECT id, name from time")
    abstract fun getTimeWithLessons(): Flowable<List<TimeWithLessons>>

    @Query("SELECT * FROM time")
    abstract fun getAllTime(): Single<List<Time>>

    @Transaction
    open fun insertLessonAndColumns(lesson: Lesson) {
        insertName(Name(name = lesson.name))
        insertTeacher(Teacher(name = lesson.teacher))
        insertBuilding(Building(name = lesson.building))
        insertClassroom(Classroom(name = lesson.classroom))
        insertType(Type(name = lesson.type))
        insertTime(Time(name = lesson.time))
        insertLesson(lesson)
    }

    @Transaction
    open fun updateLessonAndColumns(lesson: Lesson) {
        insertName(Name(name = lesson.name))
        insertTeacher(Teacher(name = lesson.teacher))
        insertBuilding(Building(name = lesson.building))
        insertClassroom(Classroom(name = lesson.classroom))
        insertType(Type(name = lesson.type))
        insertTime(Time(name = lesson.time))
        updateLesson(lesson)
    }

    @Update
    abstract fun updateLesson(lesson: Lesson)

    @Update
    abstract fun updateName(name: Name)

    @Update
    abstract fun updateTeacher(teacher: Teacher)

    @Update
    abstract fun updateBuilding(building: Building)

    @Update
    abstract fun updateClassroom(classroom: Classroom)

    @Update
    abstract fun updateType(type: Type)

    @Update
    abstract fun updateTime(time: Time)

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