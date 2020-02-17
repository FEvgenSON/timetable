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
    private var expandedPartVisible = false

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

    fun setExpandedPartVisibility(visible: Boolean) {
        if (visible == expandedPartVisible) {
            return
        }
        expandedPartVisible = visible
        if (visible) {
            itemView.divider.visibility = View.VISIBLE
            itemView.expanded_buttons.visibility = View.VISIBLE
            itemView.arrow.animate().rotation(180f)
        } else {
            itemView.divider.visibility = View.GONE
            itemView.expanded_buttons.visibility = View.GONE
            itemView.arrow.animate().rotation(0f)
        }
    }
}