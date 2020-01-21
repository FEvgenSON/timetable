package com.fevgenson.timetable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fevgenson.timetable.R
import com.fevgenson.timetable.room.entity.Lesson
import com.fevgenson.timetable.viewholder.LessonViewHolder
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LessonRecyclerViewAdapter : RecyclerView.Adapter<LessonViewHolder>() {
    private var data = listOf<Lesson>()
    private var disposable: Disposable? = null

    fun update(data: List<Lesson>) {
        disposable = Single.fromCallable {
            return@fromCallable DiffUtil.calculateDiff(
                LessonDiff(this.data, data)
            )
        }.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                this.data = data
                it.dispatchUpdatesTo(this)
                disposable?.dispose()
                disposable = null
            }, {})
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

    class LessonDiff(private val oldItems: List<Lesson>, private val newItems: List<Lesson>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].name == newItems[newItemPosition].name
        }

        override fun getOldListSize(): Int {
            return oldItems.size
        }

        override fun getNewListSize(): Int {
            return newItems.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]
            val same =
                oldItem.teacher == newItem.teacher &&
                        oldItem.building == newItem.building &&
                        oldItem.classroom == newItem.classroom &&
                        oldItem.type == newItem.type &&
                        oldItem.time == newItem.time
            //TODO: extension
            return same
        }

    }
}