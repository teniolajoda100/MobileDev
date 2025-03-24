package com.example.ouluproject

import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class TimeTableActivity : AppCompatActivity() {

    private lateinit var timetableTable: TableLayout
    private lateinit var dbHelper: TimeTableDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table)

        dbHelper = TimeTableDatabaseHelper(this)

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener { finish() }

        timetableTable = findViewById(R.id.timetableTable)

        val addTaskButton = findViewById<Button>(R.id.add_button)
        addTaskButton.setOnClickListener { showAddTaskDialog() }

        val resetTableButton = findViewById<Button>(R.id.resetTableButton)
        resetTableButton.setOnClickListener { resetTable() }

        loadTasks() // loads tasks from the database into the timetable
    }

    private fun showAddTaskDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_timetable_task, null)
        builder.setView(dialogView)

        val taskInput = dialogView.findViewById<EditText>(R.id.taskInput)
        val dayInput = dialogView.findViewById<Spinner>(R.id.dayInput)
        val colorInput = dialogView.findViewById<Spinner>(R.id.colorInput)

        // Setup dropdown options for choosing day
        val days = arrayOf("Choose Day", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        val dayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dayInput.adapter = dayAdapter

        // Setup dropdown options for choosing colour
        val colors = arrayOf("Choose Colour", "Red", "Orange", "Green", "Blue", "Purple", "Pink")
        val colorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, colors)
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        colorInput.adapter = colorAdapter

        builder.setPositiveButton("Add") { _, _ ->
            val taskText = taskInput.text.toString()
            val selectedDay = dayInput.selectedItem.toString()
            val selectedColor = colorInput.selectedItem.toString()

            if (selectedDay == "Choose Day" || selectedColor == "Choose Colour" || taskText.isBlank()) {
                Toast.makeText(this, "Invalid input. Please complete all fields.", Toast.LENGTH_SHORT).show()
            } else {
                val dayIndex = when (selectedDay) {
                    "Monday" -> 0
                    "Tuesday" -> 1
                    "Wednesday" -> 2
                    "Thursday" -> 3
                    "Friday" -> 4
                    "Saturday" -> 5
                    "Sunday" -> 6
                    else -> -1
                }

                val colorHex = when (selectedColor.lowercase()) {
                    "red" -> "#FF0000"
                    "orange" -> "#FFA500"
                    "green" -> "#008000"
                    "blue" -> "#0000FF"
                    "purple" -> "#800080"
                    "pink" -> "#FFC0CB"
                    else -> "#FFFFFF"
                }

                if (dayIndex != -1) {
                    addTaskToDay(taskText, dayIndex, colorHex)
                    dbHelper.addTask(taskText, selectedDay, colorHex) // saves tasks to database
                } else {
                    Toast.makeText(this, "Invalid day selection.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.create().show()
    }

    // Gets the existing row or creates a new one for this day
    private fun addTaskToDay(task: String, day: Int, color: String) {
        val rowIndex = day + 1
        val row = if (rowIndex < timetableTable.childCount) {
            timetableTable.getChildAt(rowIndex) as TableRow
        } else {
            TableRow(this).apply {
                for (i in 0 until 7) { // Updated from 5 to 7 to include Saturday and Sunday
                    addView(LinearLayout(this@TimeTableActivity).apply {
                        orientation = LinearLayout.VERTICAL
                        layoutParams = TableRow.LayoutParams(
                            0,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            1f
                        )
                    })
                }
                timetableTable.addView(this)
            }
        }

        // Get the column for the chosen day
        val dayColumn = row.getChildAt(day) as LinearLayout

        // Create the task view
        val taskView = TextView(this).apply {
            text = task
            setBackgroundColor(Color.parseColor(color))
            setTextColor(Color.WHITE)
            textSize = 14f
            setPadding(8, 8, 8, 8)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        // Add the task view to the selected day's column
        dayColumn.addView(taskView)
    }

    private fun loadTasks() {
        val tasks = dbHelper.getAllTasks()
        tasks.forEach { task ->
            val dayIndex = when (task["day"]) {
                "Monday" -> 0
                "Tuesday" -> 1
                "Wednesday" -> 2
                "Thursday" -> 3
                "Friday" -> 4
                "Saturday" -> 5
                "Sunday" -> 6
                else -> -1
            }

            if (dayIndex != -1) {
                addTaskToDay(task["task"]!!, dayIndex, task["color"]!!)
            }
        }
    }

    private fun resetTable() {
        timetableTable.removeViews(1, timetableTable.childCount - 1)
        dbHelper.clearTasks() // Resets table and clears tasks from database
    }
}
