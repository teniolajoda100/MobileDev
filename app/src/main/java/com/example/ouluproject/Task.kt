//Task.kt file for "My Tasks" page

package com.example.ouluproject

data class Task(
    var title: String,
    var isCompleted: Boolean = false,
    val id: Int = 0
)
