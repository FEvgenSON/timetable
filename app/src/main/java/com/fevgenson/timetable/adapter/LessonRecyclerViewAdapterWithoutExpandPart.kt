package com.fevgenson.timetable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fevgenson.timetable.R
import com.fevgenson.timetable.room.entity.Lesson
import com.fevgenson.timetable.viewholder.LessonViewHolderWithoutExpandPart

class LessonRecyclerViewAdapterWithoutExpandPart :
    RecyclerView.Adapter<LessonViewHolderWithoutExpandPart>() {
    private var data = listOf<Lesson>()

    fun update(newData: List<Lesson>) {
        val result = DiffUtil.calculateDiff(
            object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return newData[newItemPosition] == data[oldItemPosition]
                }

                override fun getOldListSize() = data.size

                override fun getNewListSize() = newData.size

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ) = true
            }
        )
        this.data = newData
        result.dispatchUpdatesTo(this)
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