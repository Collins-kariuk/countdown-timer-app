package com.example.countdown_timer_app

import androidx.room.Database
import androidx.room.RoomDatabase

// Define the database configuration and entities
@Database(entities = [Event::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    // Define the DAO that works with the database
    abstract fun eventDao(): EventDao
}
