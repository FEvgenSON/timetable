package com.fevgenson.timetable.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.fevgenson.timetable.R
import com.fevgenson.timetable.activity.CreateActivity
import com.fevgenson.timetable.adapter.LessonRecyclerViewAdapter
import com.fevgenson.timetable.viewmodel.DayViewModel
import com.fevgenson.timetable.viewmodel_factory.BaseViewModelFactory
import kotlinx.android.synthetic.main.fragment_day.*

class DayFragment : Fragment() {
    private lateinit var viewModel: DayViewModel
    private lateinit var lessonRecyclerViewAdapter: LessonRecyclerViewAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initViewModel()
    }

    private fun initRecyclerView() {
        lessonRecyclerViewAdapter = LessonRecyclerViewAdapter()
        lessonRecyclerView.adapter = lessonRecyclerViewAdapter
        lessonRecyclerView.layoutManager = LinearLayoutManager(activity)
    }

    private fun initViewModel() {
        val weekType = arguments!!.getInt(WEEK_TYPE)
        val day = arguments!!.getInt(DAY)
        viewModel =
            ViewModelProviders.of(this,
                BaseViewModelFactory {
                    DayViewModel(weekType, day)
                }
            ).get("$weekType$day", DayViewModel::class.java)
        lessonRecyclerViewAdapter.viewModel = viewModel
        lessonRecyclerViewAdapter.expandedItemsId = viewModel.expandedItemsId
        viewModel.lessons.observe(this, Observer {
            if (it.isNotEmpty()) {
                noLessonText.visibility = View.INVISIBLE
            } else {
                noLessonText.visibility = View.VISIBLE
            }
            lessonRecyclerViewAdapter.update(it)
        })
        viewModel.showEditForPosition.observe(this, Observer {
            if (it != null) {
                startCreateActivity(it)
                viewModel.showEditForPosition.value = null
            }
        })
        viewModel.showCopy.observe(this, Observer {
            if (!it) {
                return@Observer
            }
            val dialog = DialogCopyFragment.newInstance(viewModel.weekType, viewModel.day)
            dialog.resultListener =
                { weekType: Int, day: Int -> viewModel.copyDialogResult(weekType, day) }
            dialog.show(childFragmentManager, "")
            viewModel.showCopy.value = false
        })
    }

    private fun startCreateActivity(position: Int) {
        val intent = Intent(activity, CreateActivity::class.java)
        intent.putExtra(CreateActivity.DAY, viewModel.day)
        intent.putExtra(CreateActivity.WEEK_TYPE, viewModel.weekType)
        intent.putExtra(CreateActivity.POSITION, position)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        viewModel.expandedItemsId = lessonRecyclerViewAdapter.expandedItemsId
    }
}