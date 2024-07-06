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

@RequiresApi(Build.VERSION_CODES.O)
class EventViewModel(private val eventDao: EventDao) : ViewModel() {

    val events: SnapshotStateList<Event> = mutableStateListOf()

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
            events.addAll(allEvents)
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            eventDao.insertEvent(event)
            events.add(event)
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            eventDao.deleteEvent(event)
            events.remove(event)
        }
    }

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