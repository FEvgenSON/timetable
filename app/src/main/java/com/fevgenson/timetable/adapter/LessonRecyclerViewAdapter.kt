package com.fevgenson.timetable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fevgenson.timetable.R
import com.fevgenson.timetable.room.entity.Lesson
import com.fevgenson.timetable.viewholder.LessonViewHolder

class LessonRecyclerViewAdapter : RecyclerView.Adapter<LessonViewHolder>() {
    private var data = listOf<Lesson>()

    fun update(data: List<Lesson>) {
        this.data = data
        notifyDataSetChanged()//TODO: add DiffUtils
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        return LessonViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_lesson,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.onBind(data[position])
    }
}