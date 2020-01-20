package com.fevgenson.timetable.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fevgenson.timetable.R

class DayFragment : Fragment() {

    companion object {
        private const val DAY = "day"
        private const val WEEK_TYPE = "week"

        fun newInstance(day: Int, weekType: Int): DayFragment {
            val bundle = Bundle()
            bundle.putInt(DAY, day)
            bundle.putInt(WEEK_TYPE, weekType)
            val fragment = DayFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_day, container, false)
    }
}