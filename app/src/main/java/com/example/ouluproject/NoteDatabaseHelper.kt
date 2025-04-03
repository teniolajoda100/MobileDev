//Database for 'Study Notes' page

package com.example.ouluproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notes.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_CONTENT TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Insert a new note
    fun insertNote(title: String, content: String): Long {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put(COLUMN_TITLE, title)
                put(COLUMN_CONTENT, content)
            }
            val result = db.insert(TABLE_NAME, null, values)
            db.setTransactionSuccessful() // Mark the transaction as successful
            return result
        } finally {
            db.endTransaction() // End the transaction
        }
    }

    // Get all notes
    fun getAllNotes(): List<Note> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null, null, null, null, null, null
        )
        val notes = mutableListOf<Note>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val title = getString(getColumnIndexOrThrow(COLUMN_TITLE))
                val content = getString(getColumnIndexOrThrow(COLUMN_CONTENT))
                notes.add(Note(title, content, id))
            }
            close()
        }
        return notes
    }

    // Update a note
    fun updateNote(id: Int, title: String, content: String): Int {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put(COLUMN_TITLE, title)
                put(COLUMN_CONTENT, content)
            }
            val rowsUpdated = db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(id.toString()))
            db.setTransactionSuccessful() // Mark the transaction as successful
            return rowsUpdated
        } finally {
            db.endTransaction() // End the transaction
        }
    }

    // Delete a note
    fun deleteNote(id: Int): Int {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val rowsDeleted = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
            db.setTransactionSuccessful() // Mark the transaction as successful
            return rowsDeleted
        } finally {
            db.endTransaction() // End the transaction
        }
    }
}
