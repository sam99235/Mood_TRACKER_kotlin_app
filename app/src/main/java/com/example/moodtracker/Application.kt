package com.example.moodtracker

import android.app.Application

class MoodTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
    }
}