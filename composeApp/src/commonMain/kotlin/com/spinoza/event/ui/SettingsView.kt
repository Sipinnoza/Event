package com.spinoza.event.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsView() {
    var isDarkMode by remember { mutableStateOf(false) }
    var showLunarCalendar by remember { mutableStateOf(true) }
    var enableNotifications by remember { mutableStateOf(true) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "设置",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 深色模式设置
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "深色模式",
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = isDarkMode,
                onCheckedChange = { isDarkMode = it }
            )
        }
        
        HorizontalDivider()
        
        // 农历显示设置
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "显示农历",
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = showLunarCalendar,
                onCheckedChange = { showLunarCalendar = it }
            )
        }
        
        HorizontalDivider()
        
        // 通知设置
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "事件提醒",
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = enableNotifications,
                onCheckedChange = { enableNotifications = it }
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // 版本信息
        Text(
            text = "版本 1.0.0",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
} 