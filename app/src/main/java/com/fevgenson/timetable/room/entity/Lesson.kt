package com.fevgenson.timetable.room.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    foreignKeys = [ForeignKey(
        entity = Name::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("name"),
        onDelete = CASCADE,
        deferred = true
    ), ForeignKey(
        entity = Teacher::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("teacher"),
        onDelete = CASCADE,
        deferred = true
    ), ForeignKey(
        entity = Building::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("building"),
        onDelete = CASCADE,
        deferred = true
    ), ForeignKey(
        entity = Classroom::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("classroom"),
        onDelete = CASCADE,
        deferred = true
    ), ForeignKey(
        entity = Type::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("type"),
        onDelete = CASCADE,
        deferred = true
    ), ForeignKey(
        entity = Time::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("time"),
        onDelete = CASCADE,
        deferred = true
    )]
)
data class Lesson(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    var teacher: String,
    var building: String,
    var classroom: String,
    var type: String,
    var time: String,
    @ColumnInfo(index = true)
    var weekType: Int,
    @ColumnInfo(index = true)
    var day: Int
)

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Name(
    @PrimaryKey
    var name: String
)

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Teacher(
    @PrimaryKey
    var name: String
)

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Building(
    @PrimaryKey
    var name: String
)

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Classroom(
    @PrimaryKey
    var name: String
)

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Type(
    @PrimaryKey
    var name: String
)

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Time(
    @PrimaryKey
    var name: String
)

data class NameWithLessons(
    var name: String,
    @Relation(parentColumn = "name", entityColumn = "name")
    var lessons: List<Lesson>
)

data class BuildingWithLessons(
    var name: String,
    @Relation(parentColumn = "name", entityColumn = "building")
    var lessons: List<Lesson>
)

data class ClassroomWithLessons(
    var name: String,
    @Relation(parentColumn = "name", entityColumn = "classroom")
    var lessons: List<Lesson>
)

data class TypeWithLessons(
    var name: String,
    @Relation(parentColumn = "name", entityColumn = "type")
    var lessons: List<Lesson>
)

data class TimeWithLessons(
    var name: String,
    @Relation(parentColumn = "name", entityColumn = "teacher")
    var lessons: List<Lesson>
)

data class TeacherWithLessons(
    var name: String,
    @Relation(parentColumn = "name", entityColumn = "time")
    var lessons: List<Lesson>
)