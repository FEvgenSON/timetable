package com.fevgenson.timetable.fragment

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import kotlinx.android.synthetic.main.view_tab.view.*

class TimetableFragment : Fragment() {
    private lateinit var viewModel: TimetableViewModel
    private val tabColorChanger = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            setTabColor(tab!!, ContextCompat.getColor(activity!!, android.R.color.darker_gray))
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            setTabColor(tab!!, ContextCompat.getColor(activity!!, android.R.color.white))
        }
    }

    companion object {
        fun setTabColor(tab: TabLayout.Tab, color: Int) {
            val view = tab.customView!!
            view.todayImg.colorFilter = PorterDuffColorFilter(
                color, PorterDuff.Mode.SRC_IN
            )
            view.tabText.setTextColor(color)
            view.date.setTextColor(color)
        }
    }

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
        TabLayoutMediator(weekTabs, weekViewPager, true) { tab: TabLayout.Tab, position: Int ->
            tab.setCustomView(R.layout.view_tab)
            val view = tab.customView!!
            view.tabText.text = weekTabTitles[position]
            if (TimeChecker.currentWeekType == position) {
                view.todayImg.visibility = View.VISIBLE
            } else {
                view.todayImg.visibility = View.INVISIBLE
            }
            if (viewModel.savedSelectedWeekType == position) {
                setTabColor(tab, ContextCompat.getColor(activity!!, android.R.color.white))
            } else {
                setTabColor(tab, ContextCompat.getColor(activity!!, android.R.color.darker_gray))
            }
        }.attach()
        //restore select
        weekTabs.getTabAt(viewModel.savedSelectedWeekType)?.select()
        weekTabs.addOnTabSelectedListener(tabColorChanger)
        dayTabs.addOnTabSelectedListener(tabColorChanger)
        weekTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == TimeChecker.currentWeekType) {
                    dayTabs.getTabAt(TimeChecker.currentDay)?.customView?.todayImg?.visibility =
                        View.VISIBLE
                } else {
                    dayTabs.getTabAt(TimeChecker.currentDay)?.customView?.todayImg?.visibility =
                        View.GONE
                }
                for (i in 0..6) {
                    dayTabs.getTabAt(i)?.customView?.date?.text =
                        TimeChecker.dates[tab!!.position][i]
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