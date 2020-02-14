package com.fevgenson.timetable.viewholder

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fevgenson.timetable.adapter.LessonRecyclerViewAdapterWithoutExpandPart
import com.fevgenson.timetable.room.entity.ListWithLessons
import kotlinx.android.synthetic.main.view_list_item.view.*

class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var expandedPartVisible = false
    private val adapter: LessonRecyclerViewAdapterWithoutExpandPart

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
        itemView.usingLessons.layoutManager = LinearLayoutManager(itemView.context)
        adapter = LessonRecyclerViewAdapterWithoutExpandPart()
        itemView.usingLessons.adapter = adapter
    }

    fun onBind(listWithLessons: ListWithLessons) {
        itemView.name.text = listWithLessons.list
        adapter.update(listWithLessons.lessons)
    }

    fun setExpandedPartVisibility(visible: Boolean) {
        if (visible == expandedPartVisible) {
            return
        }
        expandedPartVisible = visible
        if (visible) {
            itemView.divider.visibility = View.VISIBLE
            itemView.edit_ex.visibility = View.VISIBLE
            itemView.delete_ex.visibility = View.VISIBLE
            itemView.usingLessons.visibility = View.VISIBLE
            itemView.arrow.animate().rotation(180f)
        } else {
            itemView.divider.visibility = View.GONE
            itemView.edit_ex.visibility = View.GONE
            itemView.delete_ex.visibility = View.GONE
            itemView.usingLessons.visibility = View.GONE
            itemView.arrow.animate().rotation(0f)
        }
    }
}