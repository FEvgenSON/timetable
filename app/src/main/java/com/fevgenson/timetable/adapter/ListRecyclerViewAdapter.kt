package com.fevgenson.timetable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fevgenson.timetable.R
import com.fevgenson.timetable.room.entity.ListWithLessons
import com.fevgenson.timetable.viewholder.ListViewHolder

class ListRecyclerViewAdapter : RecyclerView.Adapter<ListViewHolder>() {
    private var data: List<ListWithLessons> = mutableListOf()

    fun update(newData: List<ListWithLessons>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.onBind(data[position])
    }
}