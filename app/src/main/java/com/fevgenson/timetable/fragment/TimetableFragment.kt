package com.fevgenson.timetable.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.fevgenson.timetable.R
import com.fevgenson.timetable.activity.CreateActivity
import com.fevgenson.timetable.viewmodel.TimetableViewModel
import kotlinx.android.synthetic.main.fragment_timetable.*

class TimetableFragment : Fragment() {
    private lateinit var viewModel: TimetableViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timetable, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TimetableViewModel::class.java)
        addFloatingActionButton.setOnClickListener { startCreateActivity() }
    }

    private fun startCreateActivity() {
        val intent = Intent(activity, CreateActivity::class.java)
        intent.putExtra(CreateActivity.DAY, 0)
        intent.putExtra(CreateActivity.WEEK_TYPE, 0)
        startActivity(intent)
    }
}