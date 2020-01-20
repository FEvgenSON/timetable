package com.fevgenson.timetable.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fevgenson.timetable.room.dao.LessonDao
import com.fevgenson.timetable.room.entity.*

@Database(
    entities = [Lesson::class, Name::class, Teacher::class, Building::class, Classroom::class,
        Type::class, Time::class], version = 1, exportSchema = false
)
abstract class LessonDatabase : RoomDatabase() {
    abstract fun lessonDao(): LessonDao
}