package com.fevgenson.timetable.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fevgenson.timetable.R

class WeekFragment : Fragment() {

    companion object {
        private const val WEEK_TYPE = "weekType"

        fun newInstance(weekType: Int): WeekFragment {
            val bundle = Bundle()
            bundle.putInt(WEEK_TYPE, weekType)
            val fragment = WeekFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_week, container, false)
    }
}