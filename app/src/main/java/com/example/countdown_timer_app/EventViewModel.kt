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

@RequiresApi(Build.VERSION_CODES.O)
class EventViewModel(private val eventDao: EventDao) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    init {
        loadEvents()
    }

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

    fun addEvent(event: Event) {
        viewModelScope.launch {
            eventDao.insertEvent(event)
            _events.value += event
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            eventDao.deleteEvent(event)
            _events.value -= event
        }
    }

    fun updateEvent(updatedEvent: Event) {
        viewModelScope.launch {
            eventDao.updateEvent(updatedEvent)
            _events.value = _events.value.map { if (it.id == updatedEvent.id) updatedEvent else it }
        }
    }
}