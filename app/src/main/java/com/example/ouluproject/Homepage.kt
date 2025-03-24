// Homepage.kt

package com.example.ouluproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Homepage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        val timeTableButton = findViewById<Button>(R.id.btn_timetable_planner)
        val toDoListButton = findViewById<Button>(R.id.btn_to_do_list)
        val studyNotesButton = findViewById<Button>(R.id.btn_study_notes)
        val timerButton = findViewById<Button>(R.id.btn_timer)

        // Click to open "TimeTable Planner" page
        timeTableButton.setOnClickListener {
            val intent = Intent(this, TimeTableActivity::class.java)
            startActivity(intent)
        }




        // Click to open "My Tasks" page
        toDoListButton.setOnClickListener {
            val intent = Intent(this, TasksActivity::class.java)
            startActivity(intent)
        }
        /*
                // Click to open "Study Notes" page
                studyNotesButton.setOnClickListener {
                    val intent = Intent(this, NotesActivity::class.java)
                    startActivity(intent)
                }

                // Click to open "Study Timer" page
                timerButton.setOnClickListener {
                    val intent = Intent(this, TimerActivity::class.java)
                    startActivity(intent)
                }
            }*/
    }
}
