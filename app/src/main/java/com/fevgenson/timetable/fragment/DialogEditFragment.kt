package com.fevgenson.timetable.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.fevgenson.timetable.R
import kotlinx.android.synthetic.main.fragment_dialog_edit.*

class DialogEditFragment : DialogFragment() {
    var resultListener: ((result: String) -> Boolean)? = null

    companion object {
        private const val TEXT = "text"
        private const val TYPE = "type"
        const val EDIT = 0
        const val CREATE = 1

        fun newInstance(type: Int, text: String = ""): DialogEditFragment {
            val bundle = Bundle()
            bundle.putInt(TYPE, type)
            bundle.putString(TEXT, text)
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
        if (arguments.getInt(TYPE) == EDIT) {
            title.setText(R.string.edit)
        } else {
            title.setText(R.string.create)
        }
        newValue.setText(arguments.getString(TEXT, ""))

        saveButton.setOnClickListener {
            if (resultListener == null) {
                dismiss()
            } else {
                if (resultListener!!.invoke(newValue.text.toString())) {
                    dismiss()
                } else {
                    newValueLayout.error = getString(R.string.error_value_already_exist)
                }
            }
        }
        cancelButton.setOnClickListener {
            dismiss()
        }
    }
}