package com.fevgenson.timetable.activity

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fevgenson.timetable.R
import com.fevgenson.timetable.fragment.DialogListFragment
import com.fevgenson.timetable.room.entity.Lesson
import com.fevgenson.timetable.time.TimeChecker
import com.fevgenson.timetable.viewmodel.CreateViewModel
import com.fevgenson.timetable.viewmodel_factory.BaseViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_create.*

class CreateActivity : AppCompatActivity() {
    private lateinit var viewModel: CreateViewModel

    companion object {
        const val ID = "id"
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
        viewModel = ViewModelProvider(this, BaseViewModelFactory {
            CreateViewModel(
                intent.getIntExtra(WEEK_TYPE, -1),
                intent.getIntExtra(DAY, -1),
                intent.getIntExtra(ID, -1)
            )
        }).get(CreateViewModel::class.java)
        viewModel.lesson.observe(this, Observer { showLesson(it) })
        viewModel.nameError.observe(this, Observer { showNameError() })
        viewModel.toastError.observe(this, Observer { showErrorToast(it) })
        viewModel.finish.observe(this, Observer { finish() })
        //lists
        viewModel.names.observe(
            this,
            Observer { list -> setAutoCompleteAdapter(nameEditText, list.map { it.name }) })
        viewModel.teachers.observe(
            this,
            Observer { list -> setAutoCompleteAdapter(teacherEditText, list.map { it.name }) })
        viewModel.buildings.observe(
            this,
            Observer { list -> setAutoCompleteAdapter(buildingEditText, list.map { it.name }) })
        viewModel.classrooms.observe(
            this,
            Observer { list -> setAutoCompleteAdapter(classroomEditText, list.map { it.name }) })
        viewModel.types.observe(
            this,
            Observer { list -> setAutoCompleteAdapter(typeEditText, list.map { it.name }) })
        viewModel.times.observe(
            this,
            Observer {
                setListClickListener(
                    timeListTextView,
                    DialogListFragment.TIME,
                    it.isEmpty()
                )
            })
        setListClickListener(weekTextView, DialogListFragment.WEEK)
        setListClickListener(dayTextView, DialogListFragment.DAY)
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
        weekTextView.text = resources.getStringArray(R.array.weeks)[lesson.weekType]
        dayTextView.text = resources.getStringArray(R.array.days)[lesson.day]
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

    private fun showNameError() {
        nameLayout.error = getText(R.string.empty_string_error)
        nameLayout.setFocus()
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

    private fun setAutoCompleteAdapter(editText: AutoCompleteTextView, text: List<String>) {
        editText.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, text))
    }

    private fun setListClickListener(button: TextView, type: Int, empty: Boolean = false) {
        val resultListener: (result: String, position: Int) -> Unit =
            if (type == DialogListFragment.TIME) {
                { result, _ ->
                    startTimeTextView.text = TimeChecker.getFirstTime(result)
                    endTimeTextView.text = TimeChecker.getSecondTime(result)
                }
            } else {
                { result, position ->
                    button.text = result
                    if (type == DialogListFragment.DAY) {
                        viewModel.day = position
                    } else {
                        viewModel.weekType = position
                    }
                }
            }
        button.setOnClickListener {
            if (empty) {
                Toast.makeText(this, R.string.list_empty, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val dialog = DialogListFragment.newInstance(type)
            dialog.resultListener = resultListener
            dialog.show(supportFragmentManager, "")
        }
    }
}