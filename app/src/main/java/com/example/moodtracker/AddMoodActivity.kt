package com.example.moodtracker

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AddMoodActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var selectedMood = ""
    private lateinit var selectedMoodText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mood)

        dbHelper = DatabaseHelper(this)
        selectedMoodText = findViewById(R.id.selectedMoodText)

        // Mood buttons
        val btnHappy = findViewById<Button>(R.id.btnHappy)
        val btnSad = findViewById<Button>(R.id.btnSad)
        val btnAngry = findViewById<Button>(R.id.btnAngry)
        val btnNeutral = findViewById<Button>(R.id.btnNeutral)
        val btnExcited = findViewById<Button>(R.id.btnExcited)
        val btnSaveMood = findViewById<Button>(R.id.btnSaveMood)

        // Set mood selection listeners
        btnHappy.setOnClickListener { selectMood("😊", "Happy") }
        btnSad.setOnClickListener { selectMood("😢", "Sad") }
        btnAngry.setOnClickListener { selectMood("😠", "Angry") }
        btnNeutral.setOnClickListener { selectMood("😐", "Neutral") }
        btnExcited.setOnClickListener { selectMood("🤩", "Excited") }

        btnSaveMood.setOnClickListener {
            if (selectedMood.isNotEmpty()) {
                saveMood()
            } else {
                Toast.makeText(this, "Please select a mood first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectMood(emoji: String, moodName: String) {
        selectedMood = emoji
        selectedMoodText.text = "Selected: $emoji $moodName"
    }

    private fun saveMood() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        // Check if mood already exists for today
        val existingMood = dbHelper.getMoodForDate(today)

        if (existingMood != null) {
            // Update existing mood
            dbHelper.updateMood(existingMood.id, selectedMood, currentTime)
            Toast.makeText(this, "Mood updated for today!", Toast.LENGTH_SHORT).show()
        } else {
            // Add new mood
            val mood = Mood(0, today, selectedMood, currentTime)
            dbHelper.addMood(mood)
            Toast.makeText(this, "Mood saved!", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}