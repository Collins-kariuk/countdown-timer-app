package com.example.countdown_timer_app

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * Data Access Object (DAO) for managing event data in the database.
 *
 * This interface provides methods to perform CRUD (Create, Read, Update, Delete) operations on the
 * events table.
 */
@Dao
interface EventDao {
    /**
     * Retrieves all events from the database.
     *
     * @return A list of all events stored in the database.
     */
    @Query("SELECT * FROM events")
    suspend fun getAllEvents(): List<Event>

    /**
     * Inserts a new event into the database.
     *
     * @param event The event to be inserted.
     */
    @Insert
    suspend fun insertEvent(event: Event)

    /**
     * Deletes an existing event from the database.
     *
     * @param event The event to be deleted.
     */
    @Delete
    suspend fun deleteEvent(event: Event)
}