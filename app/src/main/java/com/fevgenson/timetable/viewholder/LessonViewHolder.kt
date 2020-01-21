package com.fevgenson.timetable.viewholder

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fevgenson.timetable.R
import com.fevgenson.timetable.room.entity.Lesson
import kotlinx.android.synthetic.main.view_lesson.view.*

class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    init {
        val color = ContextCompat.getColor(
            itemView.context,
            android.R.color.darker_gray
        )
        itemView.arrow.colorFilter =
            PorterDuffColorFilter(
                color, PorterDuff.Mode.SRC_IN
            )
        itemView.edit_ex.colorFilter = PorterDuffColorFilter(
            color, PorterDuff.Mode.SRC_IN
        )
        itemView.copy_ex.colorFilter = PorterDuffColorFilter(
            color, PorterDuff.Mode.SRC_IN
        )
    }

    fun onBind(lesson: Lesson) {
        itemView.lessonNameAndType.text =
            itemView.context.getString(R.string.main_with_additional, lesson.name, lesson.type)
        itemView.lessonTeacher.text = lesson.teacher
        itemView.lessonPlace.text =
            itemView.context.getString(
                R.string.main_with_additional,
                lesson.building,
                lesson.classroom
            )
        itemView.lessonTime.text = lesson.time
    }
}