package com.spinoza.event.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

sealed class Screen(val route: String, val title: String, val icon: @Composable () -> Unit) {
    object Calendar : Screen("calendar", "日历", { Icon(Icons.Default.DateRange, contentDescription = "日历") })
    object EventList : Screen("events", "事件列表", { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "事件列表") })
    object Settings : Screen("settings", "设置", { Icon(Icons.Default.Settings, contentDescription = "设置") })
}

@Composable
fun EventNavigationBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    val screens = listOf(Screen.Calendar, Screen.EventList, Screen.Settings)
    
    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                selected = currentScreen == screen,
                onClick = { onScreenSelected(screen) },
                icon = screen.icon,
                label = { Text(screen.title) }
            )
        }
    }
} 