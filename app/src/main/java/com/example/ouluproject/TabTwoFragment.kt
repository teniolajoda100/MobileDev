package com.example.ouluproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TabTwoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tab_two, container, false)

        val todayTimeView = view.findViewById<TextView>(R.id.todayTime)
        val sharedPref =
            requireActivity().getSharedPreferences("daily_stats", AppCompatActivity.MODE_PRIVATE)
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val timeToday = sharedPref.getLong(today, 0L)

        val todayMin = timeToday / 60000
        todayTimeView.text = "Today\n${todayMin}m"

        return view
    }
}