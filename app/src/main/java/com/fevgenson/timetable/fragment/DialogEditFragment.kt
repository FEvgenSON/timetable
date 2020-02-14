package com.fevgenson.timetable.fragment

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.fevgenson.timetable.R
import com.fevgenson.timetable.time.TimeChecker
import kotlinx.android.synthetic.main.fragment_dialog_edit.*

class DialogEditFragment : DialogFragment() {
    var resultListener: ((result: String) -> Boolean)? = null
    private var inputType = -1

    companion object {
        private const val TEXT = "text"
        private const val TYPE = "type"
        private const val INPUT_TYPE = "inputType"

        const val EDIT = 0
        const val CREATE = 1

        const val TIME = 0
        const val TEXT_NOT_NULL = 1
        const val TEXT_OR_NULL = 2

        fun newInstance(type: Int, text: String = "", inputType: Int): DialogEditFragment {
            val bundle = Bundle()
            bundle.putInt(TYPE, type)
            bundle.putString(TEXT, text)
            bundle.putInt(INPUT_TYPE, inputType)
            val fragment = DialogEditFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments!!
        inputType = arguments.getInt(INPUT_TYPE)
        if (arguments.getInt(TYPE) == CREATE) {
            title.setText(R.string.create)
        } else {
            title.setText(R.string.edit)
        }
        val text = arguments.getString(TEXT, "")
        if (inputType == TIME) {
            newValueLayout.visibility = View.GONE
            startTime.visibility = View.VISIBLE
            endTime.visibility = View.VISIBLE
            if (!text.isBlank()) {
                startTimeTextView.text = TimeChecker.getFirstTime(text)
                endTimeTextView.text = TimeChecker.getSecondTime(text)
            }
            startTimeTextView.setOnClickListener { showTimePicker(it as TextView) }
            endTimeTextView.setOnClickListener { showTimePicker(it as TextView) }
        } else {
            newValue.setText(text)
        }

        saveButton.setOnClickListener {
            if (resultListener == null) {
                dismiss()
            }
            when (inputType) {
                TEXT_OR_NULL -> if (sendResult(newValue.text.toString())) dismiss() else showError(R.string.error_value_already_exist)
                TEXT_NOT_NULL -> {
                    if (!validateText()) return@setOnClickListener
                    if (sendResult(newValue.text.toString())) dismiss() else showError(R.string.error_value_already_exist)
                }
                TIME -> {
                    if (!validateTime()) return@setOnClickListener
                    if (sendResult("${startTimeTextView.text}-${endTimeTextView.text}")) {
                        dismiss()
                    } else {
                        showError(R.string.error_value_already_exist)
                    }
                }
            }
        }
        cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun showTimePicker(textView: TextView) {
        val hour: Int
        val minutes: Int
        if (validateTime(textView.text.toString())) {
            val time = textView.text.toString()
            hour = TimeChecker.getHours(time).toInt()
            minutes = TimeChecker.getMinutes(time).toInt()
        } else {
            hour = TimeChecker.getCurrentHour()
            minutes = TimeChecker.getCurrentMinutes()
        }
        TimePickerDialog(
            activity,
            { _: TimePicker, inputHour: Int, inputMinutes: Int ->
                textView.text = String.format("%02d:%02d", inputHour, inputMinutes)
            },
            hour,
            minutes,
            true
        ).show()
    }

    private fun sendResult(result: String): Boolean {
        return resultListener!!.invoke(result)
    }

    private fun showError(errorText: Int, toastError: Boolean = false) {
        if (toastError) {
            Toast.makeText(activity, errorText, Toast.LENGTH_LONG).show()
        } else {
            newValueLayout.error = getString(errorText)
        }
    }

    private fun validateText(): Boolean {
        return if (!validateText(newValue.text.toString())) {
            showError(R.string.empty_string_error)
            false
        } else {
            true
        }
    }

    private fun validateText(text: String): Boolean {
        return !text.isBlank()
    }

    private fun validateTime(time: String): Boolean {
        return time[0] == '0' || time[0] == '1' || time[0] == '2'
    }

    private fun validateTime(): Boolean {
        val startTime = startTimeTextView.text.toString()
        val endTime = endTimeTextView.text.toString()
        if (!validateTime(startTime) || !validateTime(endTime)) {
            showError(R.string.empty_time_error, true)
            return false
        }
        if (startTime >= endTime) {
            showError(R.string.value_time_error, true)
            return false
        }
        return true
    }
}