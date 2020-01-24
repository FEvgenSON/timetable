package com.fevgenson.timetable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.fevgenson.timetable.R
import com.fevgenson.timetable.room.entity.ListWithLessons
import com.fevgenson.timetable.viewholder.ListViewHolder
import kotlinx.android.synthetic.main.view_list_item.view.*

class ListRecyclerViewAdapter : RecyclerView.Adapter<ListViewHolder>() {
    private var data: List<ListWithLessons> = mutableListOf()
    var expandedItemsId = mutableListOf<Int>()
    var editClickListener: ((id: Int) -> Unit)? = null
    var deleteClickListener: ((position: Int) -> Unit)? = null

    fun update(newData: List<ListWithLessons>) {
        val result = DiffUtil.calculateDiff(
            object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return newData[newItemPosition].list == data[newItemPosition].list
                }

                override fun getOldListSize() = data.size

                override fun getNewListSize() = newData.size

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val oldItem = data[oldItemPosition]
                    val newItem = newData[newItemPosition]
                    return if (oldItem.lessons.size == newItem.lessons.size) {
                        oldItem.lessons == newItem.lessons
                    } else {
                        false
                    }
                }
            }
        )
        data = newData
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val holder = ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_list_item, parent, false)
        )
        //expand when item click
        holder.itemView.listItem.setOnClickListener {
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
            editClickListener?.invoke(holder.adapterPosition)
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

    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.onBind(data[position])
        holder.setExpandedPartVisibility(expandedItemsId.contains(data[position].id))
    }
}