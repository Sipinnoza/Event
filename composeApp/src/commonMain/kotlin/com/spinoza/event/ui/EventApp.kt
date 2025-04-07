package com.spinoza.event.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.spinoza.event.model.Event
import com.spinoza.event.repository.EventRepository
import com.spinoza.event.ui.navigation.EventNavigationBar
import com.spinoza.event.ui.navigation.Screen

@Composable
fun EventApp(
    eventRepository: EventRepository
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }
    var refreshTrigger by remember { mutableStateOf(0) }
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Calendar) }
    
    Scaffold(
        bottomBar = {
            EventNavigationBar(
                currentScreen = currentScreen,
                onScreenSelected = { screen ->
                    currentScreen = screen
                }
            )
        },
        floatingActionButton = {
            if (currentScreen != Screen.Settings) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "添加事件")
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (currentScreen) {
                Screen.Calendar -> {
                    CalendarView(
                        eventRepository = eventRepository,
                        onEventClick = { event ->
                            selectedEvent = event
                        },
                        onAddEvent = {
                            showAddDialog = true
                        },
                        refreshTrigger = refreshTrigger
                    )
                }
                Screen.EventList -> {
                    EventListView(
                        eventRepository = eventRepository,
                        onEventClick = { event ->
                            selectedEvent = event
                        },
                        refreshTrigger = refreshTrigger
                    )
                }
                Screen.Settings -> {
                    SettingsView()
                }
            }
            
            if (showAddDialog) {
                AddEventDialog(
                    onDismiss = { showAddDialog = false },
                    onAddEvent = { event ->
                        eventRepository.addEvent(event)
                        refreshTrigger++
                        showAddDialog = false
                    }
                )
            }
            
            selectedEvent?.let { event ->
                EventDetailDialog(
                    event = event,
                    onDismiss = { selectedEvent = null },
                    onDelete = {
                        eventRepository.deleteEvent(event.id)
                        refreshTrigger++
                        selectedEvent = null
                    }
                )
            }
        }
    }
} 