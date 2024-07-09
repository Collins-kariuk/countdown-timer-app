package com.example.countdown_timer_app

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * ViewModel for managing and interacting with event data.
 *
 * @param eventDao The DAO for accessing event data from the database.
 */
@RequiresApi(Build.VERSION_CODES.O)
class EventViewModel(private val eventDao: EventDao) : ViewModel() {

    /** A list of events that updates the UI when modified. */
    val events: SnapshotStateList<Event> = mutableStateListOf()

    /** Initializes the ViewModel by loading all events. */
    init {
        loadEvents()
    }

    /**
     * Loads all events from the database and sorts them by date and time in descending order.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadEvents() {
        viewModelScope.launch {
            val allEvents = eventDao.getAllEvents().sortedByDescending {
                LocalDateTime.of(
                    LocalDate.parse(it.eventDate),
                    LocalTime.parse(it.eventTime)
                )
            }
            events.addAll(allEvents)
        }
    }

    /**
     * Adds a new event to the database and updates the list of events.
     *
     * @param event The event to be added.
     */
    fun addEvent(event: Event) {
        viewModelScope.launch {
            eventDao.insertEvent(event)
            events.add(event)
        }
    }

    /**
     * Deletes an existing event from the database and updates the list of events.
     *
     * @param event The event to be deleted.
     */
    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            eventDao.deleteEvent(event)
            events.remove(event)
        }
    }

    /**
     * Updates an existing event in the database and the list of events.
     *
     * @param updatedEvent The event with updated information.
     */
    fun updateEvent(updatedEvent: Event) {
        viewModelScope.launch {
            eventDao.updateEvent(updatedEvent)
            val index = events.indexOfFirst { it.id == updatedEvent.id }
            if (index != -1) {
                events[index] = updatedEvent
            }
        }
    }
}