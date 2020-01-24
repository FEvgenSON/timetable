package com.fevgenson.timetable.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fevgenson.timetable.R

class ListFragment : Fragment() {

    companion object {
        const val NAME = 0
        const val TEACHER = 1
        const val BUILDING = 2
        const val CLASSROOM = 3
        const val TYPE = 4
        const val TIME = 5
        private const val LIST_TYPE = "listType"

        fun newInstance(type: Int): ListFragment {
            val bundle = Bundle()
            bundle.putInt(LIST_TYPE, type)
            val fragment = ListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_list, container, false)
}