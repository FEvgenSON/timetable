package com.fevgenson.timetable.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fevgenson.timetable.room.entity.*

@Database(
    entities = [Lesson::class, Name::class, Teacher::class, Building::class, Classroom::class,
        Type::class, Time::class], version = 1
)
abstract class LessonDatabaseWithoutFlowable : RoomDatabase() {
    abstract fun lessonDaoWithoutFlowable(): LessonDaoWithoutFlowable
}