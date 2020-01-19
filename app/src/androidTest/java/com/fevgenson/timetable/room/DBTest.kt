package com.fevgenson.timetable.room

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DBTest {
    private lateinit var db: LessonDatabaseWithoutFlowable
    private lateinit var dao: LessonDaoWithoutFlowable

    @Before
    fun createDB() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            LessonDatabaseWithoutFlowable::class.java
        ).build()
        dao = db.lessonDaoWithoutFlowable()
    }

    @Test
    fun insertDifferentLessonThenRead() {
        val lessons = LessonTestHelper.createLessonList()
        lessons.forEach { dao.insertLessonAndColumns(it) }

        lessons.forEach {
            val dbLesson = dao.getLessons(it.weekType, it.day)[0]
            Assert.assertEquals(it.name, dbLesson.name)
            Assert.assertEquals(it.teacher, dbLesson.teacher)
            Assert.assertEquals(it.building, dbLesson.building)
            Assert.assertEquals(it.classroom, dbLesson.classroom)
            Assert.assertEquals(it.type, dbLesson.type)
            Assert.assertEquals(it.time, dbLesson.time)
        }
    }

    @Test
    fun update() {
        val lessons = LessonTestHelper.createLessonList()
        lessons.forEach { dao.insertLessonAndColumns(it) }

        val dbLessonForUpdate = dao.getLessons(lessons[10].weekType, lessons[10].day)[0]
        dbLessonForUpdate.time = "new time"
        dao.updateLessonAndColumns(dbLessonForUpdate)
        lessons[10].time = "new time"

        lessons.forEach {
            val dbLesson = dao.getLessons(it.weekType, it.day)[0]
            Assert.assertEquals(it.name, dbLesson.name)
            Assert.assertEquals(it.teacher, dbLesson.teacher)
            Assert.assertEquals(it.building, dbLesson.building)
            Assert.assertEquals(it.classroom, dbLesson.classroom)
            Assert.assertEquals(it.type, dbLesson.type)
            Assert.assertEquals(it.time, dbLesson.time)
        }
    }

    @Test
    fun getWithLesson() {
        val lessons = LessonTestHelper.createLessonList()
        lessons.forEach { dao.insertLessonAndColumns(it) }

        val dbLesson = dao.getNameWithLessons()[0].lessons[0]
        val lesson = lessons[0]
        Assert.assertEquals(lesson.name, dbLesson.name)
        Assert.assertEquals(lesson.teacher, dbLesson.teacher)
        Assert.assertEquals(lesson.building, dbLesson.building)
        Assert.assertEquals(lesson.classroom, dbLesson.classroom)
        Assert.assertEquals(lesson.type, dbLesson.type)
        Assert.assertEquals(lesson.time, dbLesson.time)
    }

    @After
    fun closeDB() {
        db.close()
    }
}