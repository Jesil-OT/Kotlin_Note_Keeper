package com.jesil.project.notekeeper

/**
 * Created By Jesil Toborowei
 * */

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.content_main.*

class NoteActivity : AppCompatActivity() {
    private var notePosition: Int = POSITION_NOT_SET


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val adapterCourses = ArrayAdapter<CourseInfo>(
            this,
            android.R.layout.simple_spinner_item,
            DataManger.courses.values.toList()
        )
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerCourses.adapter = adapterCourses

        notePosition = savedInstanceState?.getInt(NOTES_POSITION, POSITION_NOT_SET) ?:
            intent.getIntExtra(NOTES_POSITION, POSITION_NOT_SET)

        if (notePosition != POSITION_NOT_SET) {
            displayNote()
        } else {
            DataManger.notes.add(NoteInfo())
            notePosition = DataManger.notes.lastIndex
        }

    }

    private fun displayNote() {
        val note = DataManger.notes[notePosition]
        textNoteTittle.setText(note.title)
        textNoteText.setText(note.text)

        val coursesPosition = DataManger.courses.values.indexOf(note.course)
        spinnerCourses.setSelection(coursesPosition)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_next -> {
                moveNext()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun moveNext() {
        ++notePosition
        displayNote()
        invalidateOptionsMenu()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (notePosition >= DataManger.notes.lastIndex) {
            val menuItem = menu?.findItem(R.id.action_next)
            if (menuItem != null) {
                menuItem.icon = getDrawable(R.drawable.ic_baseline_white_24)
                menuItem.isEnabled = false
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        super.onPause()
        saveNote()
    }

    private fun saveNote() {
        val note = DataManger.notes[notePosition]
        note.title = textNoteTittle.text.toString()
        note.text = textNoteText.text.toString()
        note.course = spinnerCourses.selectedItem as CourseInfo
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTES_POSITION, notePosition)

    }

}
