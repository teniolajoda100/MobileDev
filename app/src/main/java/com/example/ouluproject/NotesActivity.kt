//NotesActivity for 'Study Notes' page

package com.example.ouluproject

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesActivity : AppCompatActivity() {

    private lateinit var noteAdapter: NoteAdapter
    private val notes = mutableListOf<Note>()
    private lateinit var dbHelper: NoteDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        dbHelper = NoteDatabaseHelper(this)

        val recyclerView = findViewById<RecyclerView>(R.id.notesRecyclerView)
        val fabAddNote = findViewById<FloatingActionButton>(R.id.fabAddNote)
        val backButton = findViewById<ImageView>(R.id.backButton)

        // Set up RecyclerView
        noteAdapter = NoteAdapter(notes, { position ->
            showEditNoteDialog(position)
        }, { position ->
            deleteNoteDialog(position)
        })
        recyclerView.adapter = noteAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // function to load notes from database
        loadNotesFromDatabase()

        // add a note Button
        fabAddNote.setOnClickListener {
            showAddNoteDialog()
        }

        // back Button
        backButton.setOnClickListener {
            finish()
        }
    }

    // load all notes from the database and update the UI
    private fun loadNotesFromDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            val notesFromDb = dbHelper.getAllNotes()
            withContext(Dispatchers.Main) {
                notes.clear()
                notes.addAll(notesFromDb)
                noteAdapter.notifyDataSetChanged()
            }
        }
    }

    // display a dialog to add a new note with title and content
    private fun showAddNoteDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_note, null)
        val titleInput = dialogView.findViewById<EditText>(R.id.noteTitleInput)
        val contentInput = dialogView.findViewById<EditText>(R.id.noteContentInput)

        val dialog = AlertDialog.Builder(this)
            .setTitle("New Note")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val title = titleInput.text.toString()
                val content = contentInput.text.toString()
                if (title.isNotBlank() && content.isNotBlank()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        dbHelper.insertNote(title, content)
                        withContext(Dispatchers.Main) {
                            loadNotesFromDatabase()
                        }
                    }
                } else {
                    Toast.makeText(this, "Both title and content are required", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    // display a dialog to edit a note
    private fun showEditNoteDialog(position: Int) {
        val note = notes[position]
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_note, null)
        val titleInput = dialogView.findViewById<EditText>(R.id.noteTitleInput)
        val contentInput = dialogView.findViewById<EditText>(R.id.noteContentInput)

        titleInput.setText(note.title)
        contentInput.setText(note.content)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit Note")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val title = titleInput.text.toString()
                val content = contentInput.text.toString()
                if (title.isNotBlank() && content.isNotBlank()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        dbHelper.updateNote(note.id, title, content)
                        withContext(Dispatchers.Main) {
                            loadNotesFromDatabase()
                        }
                    }
                } else {
                    Toast.makeText(this, "Both title and content are required", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    // show a confirmation dialog to delete a note
    private fun deleteNoteDialog(position: Int) {
        val note = notes[position]
        AlertDialog.Builder(this)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    dbHelper.deleteNote(note.id)
                    withContext(Dispatchers.Main) {
                        loadNotesFromDatabase()
                    }
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}