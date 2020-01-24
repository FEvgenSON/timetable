package com.fevgenson.timetable.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fevgenson.timetable.room.entity.ListWithLessons
import kotlinx.android.synthetic.main.view_list_item.view.*

class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onBind(listWithLessons: ListWithLessons) {
        itemView.name.text = listWithLessons.list
    }
}