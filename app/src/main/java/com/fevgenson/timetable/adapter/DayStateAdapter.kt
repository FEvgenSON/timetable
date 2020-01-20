package com.fevgenson.timetable.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fevgenson.timetable.fragment.DayFragment

class DayStateAdapter(fragment: Fragment, private val weekType: Int) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 7
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun createFragment(position: Int): Fragment {
        return DayFragment.newInstance(weekType, position)
    }
}