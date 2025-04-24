//TimerActivity.kt for "Study Timer" page

package com.example.ouluproject

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TimerActivity : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button
    private lateinit var hoursInput: EditText
    private lateinit var minutesInput: EditText
    private lateinit var secondsInput: EditText

    private var countDownTimer: CountDownTimer? = null
    private var isTimerRunning = false

    //records fro daily stats
    private var totalTimeInMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        backButton = findViewById(R.id.backButton)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        resetButton = findViewById(R.id.resetButton)
        hoursInput = findViewById(R.id.hoursInput)
        minutesInput = findViewById(R.id.minutesInput)
        secondsInput = findViewById(R.id.secondsInput)

        backButton.setOnClickListener {
            finish()
        }

        // start timer
        startButton.setOnClickListener {
            if (isTimerRunning) {
                Toast.makeText(this, "Timer is already running!", Toast.LENGTH_SHORT).show()
            } else {
                startTimer()
            }
        }

        // stop Timer
        stopButton.setOnClickListener {
            stopTimer()
        }

        // reset Timer
        resetButton.setOnClickListener {
            resetTimer()
        }
    }

    private fun startTimer() {
        // get the time from user input fields
        val hours = hoursInput.text.toString().toIntOrNull() ?: 0
        val minutes = minutesInput.text.toString().toIntOrNull() ?: 0
        val seconds = secondsInput.text.toString().toIntOrNull() ?: 0

        // calculate the total time in milliseconds
        //changed the val, add later if it doesn't work
        totalTimeInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000L

        if (totalTimeInMillis == 0L) {
            Toast.makeText(this, "Please set a valid timer!", Toast.LENGTH_SHORT).show()
            return
        }

        // start the countdown timer
        countDownTimer = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingHours = millisUntilFinished / 3600000
                val remainingMinutes = (millisUntilFinished % 3600000) / 60000
                val remainingSeconds = (millisUntilFinished % 60000) / 1000

                // update the input fields to show the remaining time
                hoursInput.setText(String.format("%02d", remainingHours))
                minutesInput.setText(String.format("%02d", remainingMinutes))
                secondsInput.setText(String.format("%02d", remainingSeconds))
            }

            override fun onFinish() {
                recordTimeUsed(totalTimeInMillis)
                // notify the user when the timer finishes
                resetTimer()
                Toast.makeText(this@TimerActivity, "Time's up!", Toast.LENGTH_SHORT).show()
            }
        }.start()

        isTimerRunning = true
    }

    // display that timer has stopped
    private fun stopTimer() {
        countDownTimer?.cancel()
        recordTimeUsed(totalTimeInMillis) //records the time you can delete later
        isTimerRunning = false
        Toast.makeText(this, "Timer stopped.", Toast.LENGTH_SHORT).show()
    }

    private fun resetTimer() {
        // cancels the timer and resets inputs to 00:00:00
        countDownTimer?.cancel()
        isTimerRunning = false
        hoursInput.setText("00")
        minutesInput.setText("00")
        secondsInput.setText("00")
        Toast.makeText(this, "Timer reset.", Toast.LENGTH_SHORT).show()
    }

    //you can delete if it doesn't work
    private fun recordTimeUsed(durationMillis: Long) {
        val sharedPref = getSharedPreferences("daily_stats", MODE_PRIVATE)
        val today = java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Date())
        val currentTotal = sharedPref.getLong(today, 0L)
        val updatedTotal = currentTotal + durationMillis
        sharedPref.edit().putLong(today, updatedTotal).apply()
    }
}