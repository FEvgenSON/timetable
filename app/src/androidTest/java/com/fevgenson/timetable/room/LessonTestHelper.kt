package com.fevgenson.timetable.room

import com.fevgenson.timetable.room.entity.Lesson

object LessonTestHelper {
    fun createLessonList(): List<Lesson> {
        val result = mutableListOf<Lesson>()
        for (i in 1..100) {
            result.add(
                Lesson(
                    name = "name$i",
                    teacher = "teacher$i",
                    building = "building$i",
                    classroom = "classroom$i",
                    type = "type$i",
                    time = "time$i",
                    day = i,
                    weekType = i
                )
            )
        }
        return result
    }
}