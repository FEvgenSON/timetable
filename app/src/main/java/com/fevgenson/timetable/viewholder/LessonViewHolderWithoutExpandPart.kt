package com.fevgenson.timetable.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fevgenson.timetable.R
import com.fevgenson.timetable.room.entity.Lesson
import kotlinx.android.synthetic.main.view_lesson_without_expand.view.*

class LessonViewHolderWithoutExpandPart(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun onBind(lesson: Lesson) {
        itemView.lessonNameAndType.text = if (lesson.type.isBlank()) lesson.name else
            itemView.context.getString(R.string.main_with_additional, lesson.name, lesson.type)
        if (lesson.teacher.isBlank()) {
            itemView.lessonTeacher.visibility = View.GONE
        } else {
            itemView.lessonTeacher.visibility = View.VISIBLE
            itemView.lessonTeacher.text = lesson.teacher
        }
        if (lesson.building.isBlank() && lesson.classroom.isBlank()) {
            itemView.lessonPlace.visibility = View.GONE
        } else {
            itemView.lessonPlace.visibility = View.VISIBLE
            itemView.lessonPlace.text = if (lesson.building.isBlank()) lesson.classroom else
                itemView.context.getString(
                    R.string.main_with_additional,
                    lesson.classroom,
                    lesson.building
                )
        }
        itemView.lessonTime.text = lesson.time
        val week = itemView.context.resources.getStringArray(R.array.weeks)[lesson.weekType]
        val day = itemView.context.resources.getStringArray(R.array.days)[lesson.day]
        itemView.lessonDate.text =
            itemView.context.getString(
                R.string.main_with_additional,
                day,
                week
            )
    }
}