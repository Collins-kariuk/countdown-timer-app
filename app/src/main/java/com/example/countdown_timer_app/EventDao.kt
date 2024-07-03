package com.example.countdown_timer_app

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EventDao {
    @Query("SELECT * FROM events")
    suspend fun getAllEvents(): List<Event>

    @Insert
    suspend fun insertEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)
}
