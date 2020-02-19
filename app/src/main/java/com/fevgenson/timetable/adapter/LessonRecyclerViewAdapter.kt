package com.fevgenson.timetable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.fevgenson.timetable.R
import com.fevgenson.timetable.room.entity.Lesson
import com.fevgenson.timetable.viewholder.LessonViewHolder
import kotlinx.android.synthetic.main.view_lesson.view.*

class LessonRecyclerViewAdapter(var currentDay: Boolean) :
    RecyclerView.Adapter<LessonViewHolder>() {
    private var data = listOf<Lesson>()
    var expandedItemsId = mutableListOf<Int>()
    var editClickListener: ((id: Int) -> Unit)? = null
    var copyClickListener: ((position: Int) -> Unit)? = null
    var deleteClickListener: ((position: Int) -> Unit)? = null

    fun update(data: List<Lesson>) {
        val result = DiffUtil.calculateDiff(
            object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return data[newItemPosition] == this@LessonRecyclerViewAdapter.data[oldItemPosition]
                }

                override fun getOldListSize() = this@LessonRecyclerViewAdapter.data.size

                override fun getNewListSize() = data.size

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ) = true
            }
        )
        this.data = data
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val holder = LessonViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_lesson, parent, false)
        )
        //expand when item click
        holder.itemView.lesson.setOnClickListener {
            val transition = ChangeBounds()
            transition.duration = 200L
            TransitionManager.beginDelayedTransition(parent, transition)
            val position = holder.adapterPosition
            val id = data[position].id
            if (expandedItemsId.contains(id)) {
                expandedItemsId.remove(id)
                holder.setExpandedPartVisibility(false)
            } else {
                expandedItemsId.add(id)
                holder.setExpandedPartVisibility(true)
            }
        }
        //edit click listener
        holder.itemView.edit_ex.setOnClickListener {
            editClickListener?.invoke(data[holder.adapterPosition].id)
        }
        //copy click listener
        holder.itemView.copy_ex.setOnClickListener {
            copyClickListener?.invoke(holder.adapterPosition)
        }
        //delete click listener
        holder.itemView.delete_ex.setOnClickListener {
            val position = holder.adapterPosition
            val id = data[position].id
            expandedItemsId.remove(id)
            deleteClickListener?.invoke(holder.adapterPosition)
        }
        return holder
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.onBind(data[position])
        holder.setExpandedPartVisibility(expandedItemsId.contains(data[position].id))
    }
}