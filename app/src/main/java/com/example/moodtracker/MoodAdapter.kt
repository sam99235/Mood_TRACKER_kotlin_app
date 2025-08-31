package com.example.moodtracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MoodAdapter(private var moods: List<Mood>) : RecyclerView.Adapter<MoodAdapter.MoodViewHolder>() {

    class MoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.textDate)
        val emojiText: TextView = itemView.findViewById(R.id.textEmoji)
        val timeText: TextView = itemView.findViewById(R.id.textTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mood, parent, false)
        return MoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        val mood = moods[position]
        holder.dateText.text = mood.date
        holder.emojiText.text = mood.emoji
        holder.timeText.text = mood.time
    }

    override fun getItemCount(): Int = moods.size

    fun updateMoods(newMoods: List<Mood>) {
        moods = newMoods
        notifyDataSetChanged()
    }
}