package com.fevgenson.timetable.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.fevgenson.timetable.R
import com.fevgenson.timetable.adapter.DayStateAdapter
import com.fevgenson.timetable.time.TimeChecker
import com.fevgenson.timetable.viewmodel.TimetableViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_timetable.*
import kotlinx.android.synthetic.main.fragment_week.*

class WeekFragment : Fragment() {
    private lateinit var viewModel: TimetableViewModel

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(parentFragment as TimetableFragment)
            .get(TimetableViewModel::class.java)
        initViewPager()
    }

    private fun initViewPager() {
        dayViewPager.offscreenPageLimit = 7
        dayViewPager.adapter = DayStateAdapter(this, arguments!!.getInt(WEEK_TYPE))
        val dayTabTitles = resources.getStringArray(R.array.days)
        TabLayoutMediator(
            (parentFragment as TimetableFragment).dayTabs,
            dayViewPager,
            true
        ) { tab: TabLayout.Tab, position: Int ->
            if (position == TimeChecker.currentDay) {
                tab.text = getString(R.string.today, dayTabTitles[position])
            } else {
                tab.text = dayTabTitles[position]
            }
        }.attach()
        //restore select
        (parentFragment as TimetableFragment).dayTabs.getTabAt(viewModel.savedSelectedDayType)
            ?.select()
    }
}