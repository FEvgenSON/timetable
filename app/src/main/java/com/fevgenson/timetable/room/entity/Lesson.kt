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
    @ColumnInfo(index = true)
    var name: String = "",
    @ColumnInfo(index = true)
    var teacher: String = "",
    @ColumnInfo(index = true)
    var building: String = "",
    @ColumnInfo(index = true)
    var classroom: String = "",
    @ColumnInfo(index = true)
    var type: String = "",
    @ColumnInfo(index = true)
    var time: String = "",
    @ColumnInfo(index = true)
    var weekType: Int = 0,
    @ColumnInfo(index = true)
    var day: Int = 0
)

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Name(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String
)

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Teacher(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String
)

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Building(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String
)

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Classroom(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String
)

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Type(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String
)

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Time(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String
)

data class NameWithLessons(
    var id: Int,
    var name: String,
    @Relation(parentColumn = "name", entityColumn = "name")
    var lessons: List<Lesson>
)

data class TeacherWithLessons(
    var id: Int,
    var name: String,
    @Relation(parentColumn = "name", entityColumn = "teacher")
    var lessons: List<Lesson>
)

data class BuildingWithLessons(
    var id: Int,
    var name: String,
    @Relation(parentColumn = "name", entityColumn = "building")
    var lessons: List<Lesson>
)

data class ClassroomWithLessons(
    var id: Int,
    var name: String,
    @Relation(parentColumn = "name", entityColumn = "classroom")
    var lessons: List<Lesson>
)

data class TypeWithLessons(
    var id: Int,
    var name: String,
    @Relation(parentColumn = "name", entityColumn = "type")
    var lessons: List<Lesson>
)

data class TimeWithLessons(
    var id: Int,
    var name: String,
    @Relation(parentColumn = "name", entityColumn = "time")
    var lessons: List<Lesson>
)

data class ListWithLessons(
    var id: Int = 0,
    var list: String,
    var lessons: List<Lesson>
) {
    companion object {
        fun getListFrom(list: NameWithLessons): ListWithLessons = ListWithLessons(
            id = list.id,
            list = list.name,
            lessons = list.lessons
        )

        fun getListFrom(list: TeacherWithLessons): ListWithLessons = ListWithLessons(
            id = list.id,
            list = list.name,
            lessons = list.lessons
        )

        fun getListFrom(list: BuildingWithLessons): ListWithLessons = ListWithLessons(
            id = list.id,
            list = list.name,
            lessons = list.lessons
        )

        fun getListFrom(list: ClassroomWithLessons): ListWithLessons = ListWithLessons(
            id = list.id,
            list = list.name,
            lessons = list.lessons
        )

        fun getListFrom(list: TypeWithLessons): ListWithLessons = ListWithLessons(
            id = list.id,
            list = list.name,
            lessons = list.lessons
        )

        fun getListFrom(list: TimeWithLessons): ListWithLessons = ListWithLessons(
            id = list.id,
            list = list.name,
            lessons = list.lessons
        )
    }
}