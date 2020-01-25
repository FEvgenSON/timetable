package com.fevgenson.timetable.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fevgenson.timetable.R
import com.fevgenson.timetable.adapter.ListRecyclerViewAdapter
import com.fevgenson.timetable.viewmodel.ListViewModel
import com.fevgenson.timetable.viewmodel_factory.BaseViewModelFactory
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {
    private lateinit var viewModel: ListViewModel
    private lateinit var adapter: ListRecyclerViewAdapter

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
        initRecyclerView()
        initViewModel()
    }

    private fun initViewModel() {
        val type = arguments!!.getInt(LIST_TYPE)
        viewModel =
            ViewModelProviders.of(this,
                BaseViewModelFactory {
                    ListViewModel(type)
                }
            ).get(type.toString(), ListViewModel::class.java)
        viewModel.data.observe(this, Observer {
            if (it.isEmpty()) {
                emptyText.visibility = View.VISIBLE
            } else {
                emptyText.visibility = View.INVISIBLE
            }
            adapter.update(it)
        })
        adapter.expandedItemsId = viewModel.expandableItems
        adapter.deleteClickListener = { viewModel.delete(it) }
        adapter.editClickListener = { startEditDialog(it) }
    }

    private fun initRecyclerView() {
        adapter = ListRecyclerViewAdapter()
        listRecyclerView.adapter = adapter
        listRecyclerView.layoutManager = LinearLayoutManager(activity)
        listRecyclerView.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun startEditDialog(position: Int) {
        val dialog = DialogEditFragment.newInstance(
            DialogEditFragment.EDIT,
            viewModel.data.value!![position].list,
            when (viewModel.type) {
                NAME -> DialogEditFragment.TEXT_NOT_NULL
                TIME -> DialogEditFragment.TIME
                else -> -1
            }
        )
        dialog.resultListener = { viewModel.edit(position, it) }
        dialog.show(childFragmentManager, "")
    }

    override fun onPause() {
        super.onPause()
        viewModel.expandableItems = adapter.expandedItemsId
    }
}