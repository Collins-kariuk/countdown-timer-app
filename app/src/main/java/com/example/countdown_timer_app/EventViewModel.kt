package com.example.countdown_timer_app

import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * ViewModel for managing and storing the state of events in the countdown timer app.
 *
 * The `EventViewModel` interacts with the `EventDao` to perform operations on the event data,
 * including loading all events, adding a new event, deleting an existing event, and updating
 * an event's details.
 *
 * The events are maintained in a `StateFlow` to observe and react to changes.
 *
 * @property eventDao The Data Access Object (DAO) used to interact with the events in the database.
 */
@RequiresApi(Build.VERSION_CODES.O)
class EventViewModel(private val eventDao: EventDao) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    init {
        loadEvents()
    }

    /**
     * Loads all events from the database and sorts them by date and time in descending order.
     * The sorted events are then updated in the `_events` StateFlow.
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
            _events.value = allEvents
        }
    }

    /**
     * Adds a new event to the database and updates the `_events` StateFlow.
     *
     * @param event The event to be added.
     */
    fun addEvent(event: Event) {
        viewModelScope.launch {
            eventDao.insertEvent(event)
            _events.value += event
        }
    }

    /**
     * Deletes an existing event from the database and updates the `_events` StateFlow.
     *
     * @param event The event to be deleted.
     */
    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            eventDao.deleteEvent(event)
            _events.value -= event
        }
    }

    /**
     * Updates the details of an existing event in the database and refreshes the `_events` StateFlow.
     *
     * @param updatedEvent The event with updated details.
     */
    fun updateEvent(updatedEvent: Event) {
        viewModelScope.launch {
            eventDao.updateEvent(updatedEvent)
            _events.value = _events.value.map { if (it.id == updatedEvent.id) updatedEvent else it }
        }
    }
}