package com.fevgenson.timetable.room

import android.content.Context
import androidx.room.Room
import com.fevgenson.timetable.room.database.LessonDatabase

object DBHolder {
    lateinit var database: LessonDatabase private set
    private var init = false

    fun init(context: Context, name: String = "debug") {
        if (!init) {
            database = Room.databaseBuilder(context, LessonDatabase::class.java, name).build()
            init = true
        }
    }
}