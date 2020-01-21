package com.fevgenson.timetable.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.fevgenson.timetable.R
import kotlinx.android.synthetic.main.fragment_dialog_copy.*

class DialogCopyFragment : DialogFragment() {
    lateinit var resultListener: ((weekType: Int, day: Int) -> Unit)

    companion object {
        private const val WEEK = "week"
        private const val DAY = "day"

        fun newInstance(weekType: Int, day: Int): DialogCopyFragment {
            val bundle = Bundle()
            bundle.putInt(WEEK, weekType)
            bundle.putInt(DAY, day)
            val fragment = DialogCopyFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_copy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weeks = resources.getStringArray(R.array.weeks)
        val days = resources.getStringArray(R.array.days)
        weekTextView.text = weeks[arguments!!.getInt(WEEK)]
        dayTextView.text = days[arguments!!.getInt(DAY)]
        weekTextView.setOnClickListener { startDialog(it as TextView, DialogListFragment.WEEK) }
        dayTextView.setOnClickListener { startDialog(it as TextView, DialogListFragment.DAY) }
        copyButton.setOnClickListener {
            resultListener.invoke(weeks.indexOf(weekTextView.text), days.indexOf(dayTextView.text))
            dismiss()
        }
        cancelButton.setOnClickListener { dismiss() }
    }

    private fun startDialog(textView: TextView, type: Int) {
        val dialog = DialogListFragment.newInstance(type)
        dialog.resultListener = { textView.text = it }
        dialog.show(fragmentManager!!, "")
    }
}