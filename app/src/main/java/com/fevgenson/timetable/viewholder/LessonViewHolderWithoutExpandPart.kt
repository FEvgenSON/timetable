package com.fevgenson.timetable.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fevgenson.timetable.R
import com.fevgenson.timetable.room.entity.Lesson
import kotlinx.android.synthetic.main.view_lesson.view.*

class LessonViewHolderWithoutExpandPart(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun onBind(lesson: Lesson) {
        itemView.lessonNameAndType.text = if (lesson.type.isBlank()) lesson.name else
            itemView.context.getString(R.string.main_with_additional, lesson.name, lesson.type)
        itemView.lessonTeacher.text = lesson.teacher
        itemView.lessonPlace.text = if (lesson.building.isBlank()) lesson.classroom else
            itemView.context.getString(
                R.string.main_with_additional,
                lesson.classroom,
                lesson.building
            )
        itemView.lessonTime.text = lesson.time
    }
}