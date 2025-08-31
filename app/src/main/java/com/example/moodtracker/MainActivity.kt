package com.example.moodtracker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var moodAdapter: MoodAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var todayMoodText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewMoods)
        todayMoodText = findViewById(R.id.todayMoodText)
        val btnAddMood = findViewById<Button>(R.id.btnAddMood)
        val btnReports = findViewById<Button>(R.id.btnReports)

        // Setup RecyclerView
        moodAdapter = MoodAdapter(dbHelper.getAllMoods())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = moodAdapter

        // Check today's mood
        updateTodayMoodDisplay()

        // Set click listeners
        btnAddMood.setOnClickListener {
            startActivity(Intent(this, AddMoodActivity::class.java))
        }

        btnReports.setOnClickListener {
            startActivity(Intent(this, ReportsActivity::class.java))
        }

        // Schedule daily notifications
        NotificationHelper.scheduleDailyNotification(this)
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when returning to main activity
        moodAdapter.updateMoods(dbHelper.getAllMoods())
        updateTodayMoodDisplay()
    }

    private fun updateTodayMoodDisplay() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val todayMood = dbHelper.getMoodForDate(today)

        if (todayMood != null) {
            todayMoodText.text = "Today's mood: ${todayMood.emoji}"
        } else {
            todayMoodText.text = "No mood recorded for today"
        }
    }
}