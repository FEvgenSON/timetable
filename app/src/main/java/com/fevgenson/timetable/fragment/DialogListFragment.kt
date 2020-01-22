package com.fevgenson.timetable.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.fevgenson.timetable.R
import com.fevgenson.timetable.activity.CreateActivity
import com.fevgenson.timetable.viewmodel.CreateViewModel
import kotlinx.android.synthetic.main.fragment_dialog_list.*

class DialogListFragment : DialogFragment() {
    private lateinit var viewModel: CreateViewModel
    var resultListener: ((result: String) -> Unit)? = null

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retainInstance = true
        val type = arguments?.getInt(FRAGMENT_TYPE)
        if (type != WEEK && type != DAY) {
            viewModel =
                ViewModelProviders.of(activity!! as CreateActivity).get(CreateViewModel::class.java)
        }

        when (type) {
            NAME -> {
                listName.setText(R.string.choose_name)
                initList(viewModel.names.value!!.map { it.name })
            }
            TEACHER -> {
                listName.setText(R.string.choose_teacher)
                initList(viewModel.teachers.value!!.map { it.name })
            }
            BUILDING -> {
                listName.setText(R.string.choose_building)
                initList(viewModel.buildings.value!!.map { it.name })
            }
            CLASSROOM -> {
                listName.setText(R.string.choose_classroom)
                initList(viewModel.classrooms.value!!.map { it.name })
            }
            TYPE -> {
                listName.setText(R.string.choose_type)
                initList(viewModel.types.value!!.map { it.name })
            }
            TIME -> {
                listName.setText(R.string.choose_time)
                initList(viewModel.times.value!!.map { it.name })
            }
            WEEK -> {
                listName.setText(R.string.choose_week)
                initList(resources.getStringArray(R.array.weeks).toList())
            }
            DAY -> {
                listName.setText(R.string.choose_day)
                initList(resources.getStringArray(R.array.days).toList())
            }
        }
    }

    private fun initList(list: List<String>) {
        this.list.adapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, list)
        this.list.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                resultListener?.invoke(list[position])
                resultListener = null
                dismiss()
            }
    }
}