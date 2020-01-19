package com.fevgenson.timetable.room

import android.content.Context
import androidx.room.Room
import com.fevgenson.timetable.room.database.LessonDatabase

object DBHolder {
    lateinit var database: LessonDatabase private set

    fun init(context: Context, name: String = "debug") {
        database = Room.databaseBuilder(context, LessonDatabase::class.java, name).build()
    }
}