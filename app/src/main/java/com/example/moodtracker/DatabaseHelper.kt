package com.example.moodtracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mood_tracker.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_MOODS = "moods"
        private const val COLUMN_ID = "id"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_EMOJI = "emoji"
        private const val COLUMN_TIME = "time"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_MOODS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DATE TEXT NOT NULL,
                $COLUMN_EMOJI TEXT NOT NULL,
                $COLUMN_TIME TEXT NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MOODS")
        onCreate(db)
    }

    fun addMood(mood: Mood): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DATE, mood.date)
            put(COLUMN_EMOJI, mood.emoji)
            put(COLUMN_TIME, mood.time)
        }
        val id = db.insert(TABLE_MOODS, null, values)
        db.close()
        return id
    }

    fun getMoodForDate(date: String): Mood? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_MOODS,
            null,
            "$COLUMN_DATE = ?",
            arrayOf(date),
            null,
            null,
            null
        )

        var mood: Mood? = null
        if (cursor.moveToFirst()) {
            mood = Mood(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                emoji = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMOJI)),
                time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))
            )
        }
        cursor.close()
        db.close()
        return mood
    }

    fun updateMood(id: Long, emoji: String, time: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EMOJI, emoji)
            put(COLUMN_TIME, time)
        }
        val rowsAffected = db.update(TABLE_MOODS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return rowsAffected
    }

    fun getAllMoods(): List<Mood> {
        val moodList = mutableListOf<Mood>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_MOODS, null, null, null, null, null, "$COLUMN_DATE DESC")

        if (cursor.moveToFirst()) {
            do {
                val mood = Mood(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                    emoji = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMOJI)),
                    time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))
                )
                moodList.add(mood)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return moodList
    }
}