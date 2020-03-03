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
    var resultListener: ((weekType: Int, day: Int) -> Unit)? = null
    var day = -1
    var weekType = -1

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
        day = arguments!!.getInt(DAY)
        weekType = arguments!!.getInt(WEEK)
        weekTextView.text = resources.getStringArray(R.array.weeks)[weekType]
        dayTextView.text = resources.getStringArray(R.array.days)[day]
        weekTextView.setOnClickListener { startDialog(it as TextView, DialogListFragment.WEEK) }
        dayTextView.setOnClickListener { startDialog(it as TextView, DialogListFragment.DAY) }
        copyButton.setOnClickListener {
            resultListener?.invoke(weekType, day)
            resultListener = null
            dismiss()
        }
        cancelButton.setOnClickListener {
            resultListener = null
            dismiss()
        }
    }

    private fun startDialog(textView: TextView, type: Int) {
        val dialog = DialogListFragment.newInstance(type)
        dialog.resultListener = { result, position ->
            textView.text = result
            if (type == DialogListFragment.DAY) {
                day = position
            } else {
                weekType = position
            }
        }
        dialog.show(childFragmentManager, "")
    }
}