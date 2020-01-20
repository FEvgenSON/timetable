package com.fevgenson.timetable.fragment

import android.os.Bundle
import androidx.fragment.app.DialogFragment

class DialogListFragment : DialogFragment() {

    companion object {
        const val NAME = 0
        const val TEACHER = 1
        const val BUILDING = 2
        const val CLASSROOM = 3
        const val TYPE = 4
        const val TIME = 5
        const val WEEK = 6
        const val DAY = 7
        private const val FRAGMENT_TYPE = "fragmentType"

        fun newInstance(type: Int): DialogListFragment {
            val bundle = Bundle()
            bundle.putInt(FRAGMENT_TYPE, type)
            val fragment = DialogListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}