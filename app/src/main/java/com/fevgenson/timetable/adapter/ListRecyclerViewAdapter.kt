package com.fevgenson.timetable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.fevgenson.timetable.R
import com.fevgenson.timetable.room.entity.ListWithLessons
import com.fevgenson.timetable.viewholder.ListViewHolder
import kotlinx.android.synthetic.main.view_list_item.view.*

class ListRecyclerViewAdapter : RecyclerView.Adapter<ListViewHolder>() {
    private var data: List<ListWithLessons> = mutableListOf()
    var expandedItemsId = mutableListOf<String>()

    fun update(newData: List<ListWithLessons>) {
        data = newData
        notifyDataSetChanged()
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
            val id = data[position].list
            if (expandedItemsId.contains(id)) {
                expandedItemsId.remove(id)
                holder.setExpandedPartVisibility(false)
            } else {
                expandedItemsId.add(id)
                holder.setExpandedPartVisibility(true)
            }
        }
        return holder
    }

    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.onBind(data[position])
    }
}