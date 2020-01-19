package com.fevgenson.timetable.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.fevgenson.timetable.R
import kotlinx.android.synthetic.main.activity_create.*

class CreateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        //action bar
        setSupportActionBar(createActivityToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
}