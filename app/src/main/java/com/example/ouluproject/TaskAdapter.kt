//TaskAdapter.kt file for "My Tasks" page

package com.example.ouluproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val dbHelper: TaskDatabaseHelper,
    private val onTaskClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {


    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskTitle: TextView = itemView.findViewById(R.id.taskTitle)
        val checkBoxTask: CheckBox = itemView.findViewById(R.id.checkBoxTask)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskTitle.text = task.title
        holder.checkBoxTask.isChecked = task.isCompleted

        holder.checkBoxTask.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
            dbHelper.updateTask(task.id, task.title, isChecked) // updates task completion status
        }

        holder.deleteButton.setOnClickListener {
            val removedTask = tasks.removeAt(position)
            dbHelper.deleteTask(removedTask.id) // delete task from database
            notifyItemRemoved(position)
        }

        holder.itemView.setOnClickListener {
            onTaskClicked(position) // task clicking for editing tasks
        }
    }

    override fun getItemCount(): Int = tasks.size
}
