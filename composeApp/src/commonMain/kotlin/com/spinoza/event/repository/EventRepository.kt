package com.spinoza.event.repository

import com.spinoza.event.model.Event
import kotlinx.datetime.LocalDateTime

class EventRepository {
    private val events = mutableListOf<Event>()
    
    fun addEvent(event: Event) {
        events.add(event)
    }
    
    fun getEvents(): List<Event> = events.toList()
    
    fun     getEventsForDay(date: LocalDateTime): List<Event> {
        return events.filter { event ->
            event.startTime.date == date.date
        }
    }
    
    fun deleteEvent(eventId: String) {
        val event = events.find { it.id == eventId }
        events.remove(event)
    }
} 