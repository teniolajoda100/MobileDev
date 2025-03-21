//Database file for "TimeTable Planner" page

package com.example.ouluproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TimeTableDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "timetable.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "tasks"
        const val COLUMN_ID = "id"
        const val COLUMN_TASK = "task"
        const val COLUMN_DAY = "day"
        const val COLUMN_COLOR = "color"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TASK TEXT NOT NULL,
                $COLUMN_DAY TEXT NOT NULL,
                $COLUMN_COLOR TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addTask(task: String, day: String, color: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TASK, task)
            put(COLUMN_DAY, day)
            put(COLUMN_COLOR, color)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllTasks(): List<Map<String, String>> {
        val tasks = mutableListOf<Map<String, String>>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val task = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK))
            val day = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY))
            val color = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COLOR))
            tasks.add(mapOf("task" to task, "day" to day, "color" to color))
        }
        cursor.close()
        return tasks
    }

    fun clearTasks() {
        writableDatabase.delete(TABLE_NAME, null, null)
    }
}
