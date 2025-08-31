package com.example.moodtracker

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class ReportsActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var reportText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var reportAdapter: MoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        dbHelper = DatabaseHelper(this)
        reportText = findViewById(R.id.reportText)
        recyclerView = findViewById(R.id.recyclerViewReport)

        val btnWeekReport = findViewById<Button>(R.id.btnWeekReport)
        val btnMonthReport = findViewById<Button>(R.id.btnMonthReport)

        // Setup RecyclerView
        reportAdapter = MoodAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = reportAdapter

        btnWeekReport.setOnClickListener { showWeekReport() }
        btnMonthReport.setOnClickListener { showMonthReport() }

        // Show month report by default
        showMonthReport()
    }

    private fun showWeekReport() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val weekAgo = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        val moods = dbHelper.getMoodsInDateRange(weekAgo, getCurrentDate())
        reportAdapter.updateMoods(moods)

        val moodCounts = countMoods(moods)
        reportText.text = "Last 7 Days Report:\n${formatMoodCounts(moodCounts)}\nTotal entries: ${moods.size}"
    }

    private fun showMonthReport() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -30)
        val monthAgo = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        val moods = dbHelper.getMoodsInDateRange(monthAgo, getCurrentDate())
        reportAdapter.updateMoods(moods)

        val moodCounts = countMoods(moods)
        reportText.text = "Last 30 Days Report:\n${formatMoodCounts(moodCounts)}\nTotal entries: ${moods.size}"
    }

    private fun countMoods(moods: List<Mood>): Map<String, Int> {
        val counts = mutableMapOf<String, Int>()
        for (mood in moods) {
            counts[mood.emoji] = counts.getOrDefault(mood.emoji, 0) + 1
        }
        return counts
    }

    private fun formatMoodCounts(counts: Map<String, Int>): String {
        return counts.entries.joinToString("\n") { "${it.key}: ${it.value} times" }
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}