package com.example.moodtracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "mood_tracker.db"
        const val DATABASE_VERSION = 1
        const val TABLE_MOODS = "moods"
        const val COLUMN_ID = "id"
        const val COLUMN_DATE = "date"
        const val COLUMN_EMOJI = "emoji"
        const val COLUMN_TIME = "time"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_MOODS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DATE TEXT NOT NULL UNIQUE,
                $COLUMN_EMOJI TEXT NOT NULL,
                $COLUMN_TIME TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOODS")
        onCreate(db)
    }

    fun addMood(mood: Mood): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DATE, mood.date)
            put(COLUMN_EMOJI, mood.emoji)
            put(COLUMN_TIME, mood.time)
        }
        return db.insert(TABLE_MOODS, null, values)
    }

    fun getAllMoods(): List<Mood> {
        val moods = mutableListOf<Mood>()
        val db = readableDatabase
        val cursor = db.query(TABLE_MOODS, null, null, null, null, null, "$COLUMN_DATE DESC")

        while (cursor.moveToNext()) {
            val mood = Mood(
                cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMOJI)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))
            )
            moods.add(mood)
        }
        cursor.close()
        return moods
    }

    fun getMoodForDate(date: String): Mood? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_MOODS,
            null,
            "$COLUMN_DATE = ?",
            arrayOf(date),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val mood = Mood(
                cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMOJI)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))
            )
            cursor.close()
            mood
        } else {
            cursor.close()
            null
        }
    }

    fun getMoodsInDateRange(startDate: String, endDate: String): List<Mood> {
        val moods = mutableListOf<Mood>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_MOODS,
            null,
            "$COLUMN_DATE BETWEEN ? AND ?",
            arrayOf(startDate, endDate),
            null,
            null,
            "$COLUMN_DATE DESC"
        )

        while (cursor.moveToNext()) {
            val mood = Mood(
                cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMOJI)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))
            )
            moods.add(mood)
        }
        cursor.close()
        return moods
    }

    fun updateMood(id: Long, emoji: String, time: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EMOJI, emoji)
            put(COLUMN_TIME, time)
        }
        return db.update(TABLE_MOODS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}