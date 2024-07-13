package com.example.countdown_timer_app

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventName: String,
    val eventDate: String,
    val eventTime: String,
    val eventNotes: String?,
    val eventLocation: String
)
