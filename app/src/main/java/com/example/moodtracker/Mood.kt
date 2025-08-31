package com.example.moodtracker

data class Mood(
    val id: Long = 0,
    val date: String,
    val emoji: String,
    val time: String
)