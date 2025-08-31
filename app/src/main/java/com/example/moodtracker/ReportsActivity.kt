package com.example.moodtracker

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ReportsActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        dbHelper = DatabaseHelper(this)
        
        val reportText = findViewById<TextView>(R.id.reportText)
        
        // Generate simple mood report
        val moods = dbHelper.getAllMoods()
        val moodCounts = moods.groupBy { it.emoji }.mapValues { it.value.size }
        
        val reportBuilder = StringBuilder()
        reportBuilder.append("Mood Report\n\n")
        reportBuilder.append("Total entries: ${moods.size}\n\n")
        
        moodCounts.forEach { (emoji, count) ->
            reportBuilder.append("$emoji: $count times\n")
        }
        
        reportText.text = reportBuilder.toString()
    }
}