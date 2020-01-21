package com.fevgenson.timetable.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.fevgenson.timetable.R
import com.fevgenson.timetable.activity.CreateActivity
import com.fevgenson.timetable.adapter.WeekStateAdapter
import com.fevgenson.timetable.time.TimeChecker
import com.fevgenson.timetable.viewmodel.TimetableViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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
        initViewPager()
    }

    private fun initViewPager() {
        weekViewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        weekViewPager.isUserInputEnabled = false
        weekViewPager.offscreenPageLimit = 2
        weekViewPager.adapter = WeekStateAdapter(this)
        val weekTabTitles = resources.getStringArray(R.array.weeks)
        val dayTabTitles = resources.getStringArray(R.array.days)
        TabLayoutMediator(weekTabs, weekViewPager, true) { tab: TabLayout.Tab, position: Int ->
            if (position == TimeChecker.currentWeekType) {
                tab.text = getString(R.string.today, weekTabTitles[position])
            } else {
                tab.text = weekTabTitles[position]
            }
        }.attach()
        //restore select
        weekTabs.getTabAt(viewModel.savedSelectedWeekType)?.select()
        weekTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == TimeChecker.currentWeekType) {
                    dayTabs.getTabAt(TimeChecker.currentDay)?.text =
                        getString(R.string.today, dayTabTitles[TimeChecker.currentDay])
                } else {
                    dayTabs.getTabAt(TimeChecker.currentDay)?.text =
                        dayTabTitles[TimeChecker.currentDay]
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.savedSelectedWeekType = weekTabs.selectedTabPosition
        viewModel.savedSelectedDayType = dayTabs.selectedTabPosition
    }

    private fun startCreateActivity() {
        val intent = Intent(activity, CreateActivity::class.java)
        intent.putExtra(CreateActivity.DAY, dayTabs.selectedTabPosition)
        intent.putExtra(CreateActivity.WEEK_TYPE, weekTabs.selectedTabPosition)
        startActivity(intent)
    }
}