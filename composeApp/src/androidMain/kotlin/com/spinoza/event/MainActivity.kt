package com.spinoza.event

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.spinoza.event.repository.EventRepository
import com.spinoza.event.ui.EventApp

class MainActivity : ComponentActivity() {
    private val eventRepository = EventRepository()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            EventApp(eventRepository)
        }
    }
}