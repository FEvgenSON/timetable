package com.fevgenson.timetable.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.fevgenson.timetable.R
import com.fevgenson.timetable.viewmodel.ListViewModel
import com.fevgenson.timetable.viewmodel_factory.BaseViewModelFactory

class ListFragment : Fragment() {
    private lateinit var viewModel: ListViewModel

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arguments!!.getInt(LIST_TYPE)
        viewModel =
            ViewModelProviders.of(this,
                BaseViewModelFactory {
                    ListViewModel(type)
                }
            ).get(type.toString(), ListViewModel::class.java)
    }
}