package com.example.sqlitenotesapp

import DatabaseHelper
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var btnSave: Button
    private lateinit var listNotes: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.sqlitenotesapp.R.layout.activity_main)

        etTitle = findViewById(com.example.sqlitenotesapp.R.id.et_title)
        etContent = findViewById(com.example.sqlitenotesapp.R.id.et_content)
        btnSave = findViewById(com.example.sqlitenotesapp.R.id.btn_save)
        listNotes = findViewById(com.example.sqlitenotesapp.R.id.list_notes)

        dbHelper = DatabaseHelper(this)

        btnSave.setOnClickListener { saveNote() }
        loadNotes()
    }

    private fun saveNote() {
        val title = etTitle.text.toString().trim()
        val content = etContent.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
            return
        }

        val id = dbHelper.insertNote(title, content)
        if (id != -1L) {
            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show()
            etTitle.setText("")
            etContent.setText("")
            loadNotes()
        } else {
            Toast.makeText(this, "Failed to save note", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadNotes() {
        val notes = dbHelper.allNotes
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notes)
        listNotes.adapter = adapter
        listNotes.setOnItemLongClickListener { _, _, position, _ ->
            val note = notes[position]
            val noteId = note.id


            val rowsDeleted = dbHelper.deleteNote(noteId)

            if (rowsDeleted > 0) {
                Toast.makeText(this, "Note deleted!", Toast.LENGTH_SHORT).show()
                loadNotes()
            }

            true
        }
    }
}
