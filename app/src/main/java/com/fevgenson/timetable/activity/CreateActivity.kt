package com.fevgenson.timetable.activity

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fevgenson.timetable.R
import com.fevgenson.timetable.room.entity.Lesson
import com.fevgenson.timetable.time.TimeChecker
import com.fevgenson.timetable.viewmodel.CreateViewModel
import com.fevgenson.timetable.viewmodel_factory.BaseViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_create.*

class CreateActivity : AppCompatActivity() {
    private lateinit var viewModel: CreateViewModel

    companion object {
        const val POSITION = "position"
        const val WEEK_TYPE = "weekType"
        const val DAY = "day"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        //action bar
        setSupportActionBar(createActivityToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //viewModel
        viewModel =
            ViewModelProviders.of(this,
                BaseViewModelFactory {
                    CreateViewModel(
                        intent.getIntExtra(WEEK_TYPE, -1),
                        intent.getIntExtra(DAY, -1),
                        intent.getIntExtra(POSITION, -1)
                    )
                }
            ).get(CreateViewModel::class.java)
        viewModel.lesson.observe(this, Observer { showLesson(it) })
        viewModel.layoutError.observe(this, Observer { showError(it) })
        viewModel.toastError.observe(this, Observer { showErrorToast(it) })
        viewModel.finish.observe(this, Observer { finish() })
        //OnClickListeners
        saveFloatingActionButton.setOnClickListener {
            viewModel.save(
                name = nameEditText.text.toString(),
                teacher = teacherEditText.text.toString(),
                building = buildingEditText.text.toString(),
                classroom = classroomEditText.text.toString(),
                type = typeEditText.text.toString(),
                startTime = startTimeTextView.text.toString(),
                endTime = endTimeTextView.text.toString()
            )
        }
        startTimeTextView.setOnClickListener { showTimePicker(it as TextView) }
        endTimeTextView.setOnClickListener { showTimePicker(it as TextView) }
        //Error hider
        nameLayout.autoHidingError()
        teacherLayout.autoHidingError()
        buildingLayout.autoHidingError()
        classroomLayout.autoHidingError()
        typeLayout.autoHidingError()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_activity_actionbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.clear -> clear()
        }
        return true
    }

    private fun clear() {
        nameEditText.text = null
        teacherEditText.text = null
        buildingEditText.text = null
        classroomEditText.text = null
        typeEditText.text = null
        startTimeTextView.setText(R.string.set)
        endTimeTextView.setText(R.string.set)
    }

    private fun showLesson(lesson: Lesson) {
        if (lesson.time.isBlank()) {
            supportActionBar?.setTitle(R.string.create)
            return
        }
        supportActionBar?.setTitle(R.string.edit)
        nameEditText.setText(lesson.name)
        teacherEditText.setText(lesson.teacher)
        buildingEditText.setText(lesson.building)
        classroomEditText.setText(lesson.classroom)
        typeEditText.setText(lesson.type)
        startTimeTextView.text = TimeChecker.getFirstTime(lesson.time)
        endTimeTextView.text = TimeChecker.getSecondTime(lesson.time)
    }

    private fun TextInputLayout.autoHidingError() {
        editText?.addTextChangedListener { this.error = null }
    }

    private fun TextInputLayout.setFocus() {
        requestFocus()
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
            editText,
            InputMethodManager.SHOW_IMPLICIT
        )
    }

    private fun showError(id: Int) {
        val textInputLayout = findViewById<TextInputLayout>(id)
        textInputLayout.error = getText(R.string.empty_string_error)
        textInputLayout.setFocus()
    }

    private fun showErrorToast(message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showTimePicker(textView: TextView) {
        val hour: Int
        val minutes: Int
        if (viewModel.validateTime(textView.text.toString())) {
            val time = textView.text.toString()
            hour = TimeChecker.getHours(time).toInt()
            minutes = TimeChecker.getMinutes(time).toInt()
        } else {
            hour = TimeChecker.getCurrentHour()
            minutes = TimeChecker.getCurrentMinutes()
        }
        TimePickerDialog(
            this,
            { _: TimePicker, inputHour: Int, inputMinutes: Int ->
                textView.text = String.format("%02d:%02d", inputHour, inputMinutes)
            },
            hour,
            minutes,
            true
        ).show()
    }
}