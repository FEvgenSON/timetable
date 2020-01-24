package com.fevgenson.timetable.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.fevgenson.timetable.R
import com.fevgenson.timetable.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_dialog_edit.*

class DialogEditFragment : DialogFragment() {
    var viewModel: ListViewModel? = null
    var position = -1

    companion object {
        private const val POSITION = "position"

        fun newInstance(position: Int): DialogEditFragment {
            val bundle = Bundle()
            bundle.putInt(POSITION, position)
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
        position = arguments!!.getInt(POSITION)
        saveButton.setOnClickListener {
            if (viewModel!!.edit(position, newValue.text.toString())) {
                dismiss()
            }
        }
        cancelButton.setOnClickListener {
            dismiss()
        }
        newValue.setText(viewModel!!.data.value!![position].list)
    }

    fun attachViewModel(viewModel: ListViewModel) {
        this.viewModel = viewModel
    }
}