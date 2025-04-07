package com.spinoza.event

import androidx.compose.ui.window.ComposeUIViewController
import com.spinoza.event.repository.EventRepository
import com.spinoza.event.ui.EventApp

fun MainViewController() = ComposeUIViewController {
    val repository = EventRepository()
    EventApp(repository)
}