package com.fevgenson.timetable.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fevgenson.timetable.R
import com.fevgenson.timetable.adapter.ListStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_dictionary.*

class DictionaryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_dictionary, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        listViewPager.offscreenPageLimit = 6
        listViewPager.adapter = ListStateAdapter(this)
        val tabTitles = resources.getStringArray(R.array.lists)
        TabLayoutMediator(
            listTypeTabs,
            listViewPager,
            true
        ) { tab: TabLayout.Tab, position: Int -> tab.text = tabTitles[position] }.attach()
    }
}