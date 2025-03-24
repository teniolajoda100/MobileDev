//TasksActivity.kt file for "My Tasks" page

package com.example.ouluproject

import android.widget.ImageView
import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TasksActivity : AppCompatActivity() {
    private lateinit var taskAdapter: TaskAdapter
    private val tasks = mutableListOf<Task>()
    private lateinit var dbHelper: TaskDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        dbHelper = TaskDatabaseHelper(this)
        tasks.addAll(dbHelper.getAllTasks()) // loads tasks from database

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val fabAddTask = findViewById<FloatingActionButton>(R.id.fabAddTask)
        val backButton = findViewById<ImageView>(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }

        taskAdapter = TaskAdapter(tasks, dbHelper) { position ->
            showEditTaskDialog(position)
        }
        recyclerView.adapter = taskAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        fabAddTask.setOnClickListener {
            showAddTaskDialog()
        }
    }

    // display a dialog to add a new task with a title
    private fun showAddTaskDialog() {
        val editText = EditText(this)
        editText.hint = "Enter task title"

        AlertDialog.Builder(this)
            .setTitle("New Task")
            .setView(editText)
            .setPositiveButton("Add") { _, _ ->
                val taskTitle = editText.text.toString()
                if (taskTitle.isNotBlank()) {
                    val id = dbHelper.insertTask(taskTitle) // save new task to database
                    val newTask = Task(taskTitle, false, id.toInt())
                    tasks.add(newTask)
                    taskAdapter.notifyItemInserted(tasks.size - 1)
                } else {
                    Toast.makeText(this, "Task title cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // display a dialog to edit the title of a task.
    private fun showEditTaskDialog(position: Int) {
        val editText = EditText(this)
        editText.setText(tasks[position].title)

        AlertDialog.Builder(this)
            .setTitle("Edit Task")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                val newTitle = editText.text.toString()
                if (newTitle.isNotBlank()) {
                    val task = tasks[position]
                    task.title = newTitle
                    dbHelper.updateTask(task.id, newTitle, task.isCompleted) // update task in database
                    taskAdapter.notifyItemChanged(position)
                } else {
                    Toast.makeText(this, "Task title cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
