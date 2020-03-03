package com.fevgenson.timetable.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.fevgenson.timetable.R
import com.fevgenson.timetable.activity.CreateActivity
import com.fevgenson.timetable.viewmodel.CreateViewModel
import kotlinx.android.synthetic.main.fragment_dialog_list.*

class DialogListFragment : DialogFragment() {
    var resultListener: ((result: String, position: Int) -> Unit)? = null

    companion object {
        const val TIME = 0
        const val WEEK = 1
        const val DAY = 2
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

        when (arguments?.getInt(FRAGMENT_TYPE)) {
            TIME -> {
                val viewModel =
                    ViewModelProvider(activity!! as CreateActivity)
                        .get(CreateViewModel::class.java)
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
                resultListener?.invoke(list[position], position)
                resultListener = null
                dismiss()
            }
    }
}