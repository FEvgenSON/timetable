package com.fevgenson.timetable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fevgenson.timetable.R
import com.fevgenson.timetable.room.entity.Lesson
import com.fevgenson.timetable.viewholder.LessonViewHolderWithoutExpandPart

class LessonRecyclerViewAdapterWithoutExpandPart :
    RecyclerView.Adapter<LessonViewHolderWithoutExpandPart>() {
    private var data = listOf<Lesson>()

    fun update(data: List<Lesson>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LessonViewHolderWithoutExpandPart =
        LessonViewHolderWithoutExpandPart(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_lesson_without_expand,
                parent,
                false
            )
        )

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: LessonViewHolderWithoutExpandPart, position: Int) {
        holder.onBind(data[position])
    }
}