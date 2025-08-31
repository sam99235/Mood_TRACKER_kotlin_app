package com.example.moodtracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var moodAdapter: MoodAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        
        // Initialize views
        val btnAddMood = findViewById<Button>(R.id.btnAddMood)
        val btnReports = findViewById<Button>(R.id.btnReports)
        recyclerView = findViewById(R.id.recyclerViewMoods)

        // Set up RecyclerView
        setupRecyclerView()

        // Button click listeners
        btnAddMood.setOnClickListener {
            val intent = Intent(this, AddMoodActivity::class.java)
            startActivity(intent)
        }

        btnReports.setOnClickListener {
            val intent = Intent(this, ReportsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadMoods()
    }

    private fun setupRecyclerView() {
        moodAdapter = MoodAdapter(emptyList())
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = moodAdapter
        }
    }

    private fun loadMoods() {
        val moods = dbHelper.getAllMoods()
        moodAdapter.updateMoods(moods)
    }
}