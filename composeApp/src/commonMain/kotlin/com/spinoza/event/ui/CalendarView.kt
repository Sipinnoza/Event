package com.spinoza.event.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.spinoza.event.model.Event
import com.spinoza.event.repository.EventRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.LocalDate
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarView(
    eventRepository: EventRepository,
    onEventClick: (Event) -> Unit,
    onAddEvent: () -> Unit,
    refreshTrigger: Int
) {
    var selectedDate by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())) }
    var currentMonth by remember { mutableStateOf(selectedDate) }
    var showCalendar by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()
    val events = remember(selectedDate, refreshTrigger) {
        eventRepository.getEventsForDay(selectedDate).sortedBy { it.startTime }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "日历事件",
                        style = MaterialTheme.typography.titleMedium
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.height(56.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddEvent,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加事件")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 月份选择器
            MonthPicker(
                currentDate = currentMonth,
                onDateSelected = { date ->
                    currentMonth = date
                    selectedDate = date
                },
                onToggleCalendar = { showCalendar = !showCalendar }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 日期选择器
            if (showCalendar) {
                DatePicker(
                    currentMonth = currentMonth,
                    selectedDate = selectedDate,
                    onDateSelected = { date ->
                        selectedDate = date
                        showCalendar = false
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // 日期卡片
            DateCard(
                date = selectedDate,
                eventCount = events.size,
                isScrolled = scrollState.firstVisibleItemScrollOffset > 10 || scrollState.firstVisibleItemIndex > 0
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 事件列表
            if (events.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "今天没有事件",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            } else {
                EventList(
                    events = events,
                    onEventClick = onEventClick,
                    scrollState = scrollState
                )
            }
        }
    }
}

@Composable
fun MonthPicker(
    currentDate: LocalDateTime,
    onDateSelected: (LocalDateTime) -> Unit,
    onToggleCalendar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    val newDate = currentDate.date.minus(1, DateTimeUnit.MONTH)
                    onDateSelected(newDate.atTime(currentDate.time))
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
            ) {
                Icon(
                    Icons.Default.KeyboardArrowUp,
                    contentDescription = "上个月",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = "${currentDate.year}年${currentDate.monthNumber}月",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.clickable(onClick = onToggleCalendar)
            )

            IconButton(
                onClick = {
                    val newDate = currentDate.date.plus(1, DateTimeUnit.MONTH)
                    onDateSelected(newDate.atTime(currentDate.time))
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
            ) {
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "下个月",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun DatePicker(
    currentMonth: LocalDateTime,
    selectedDate: LocalDateTime,
    onDateSelected: (LocalDateTime) -> Unit
) {
    val firstDayOfMonth = currentMonth.date.minus(currentMonth.dayOfMonth - 1, DateTimeUnit.DAY)
    val daysInMonth = when (currentMonth.month) {
        Month.JANUARY -> 31
        Month.FEBRUARY -> if (currentMonth.year % 4 == 0 && (currentMonth.year % 100 != 0 || currentMonth.year % 400 == 0)) 29 else 28
        Month.MARCH -> 31
        Month.APRIL -> 30
        Month.MAY -> 31
        Month.JUNE -> 30
        Month.JULY -> 31
        Month.AUGUST -> 31
        Month.SEPTEMBER -> 30
        Month.OCTOBER -> 31
        Month.NOVEMBER -> 30
        Month.DECEMBER -> 31
        else -> 0
    }
    
    // 获取月初是星期几（1-7，其中7代表星期日）
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value
    // 调整为以星期日为一周的第一天（0-6）
    val adjustedFirstDayOfWeek = if (firstDayOfWeek == 7) 0 else firstDayOfWeek
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 星期标题行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("日", "一", "二", "三", "四", "五", "六").forEach { day ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        // 日期网格
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            var currentDay = 1
            while (currentDay <= daysInMonth) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // 填充每行7天
                    for (i in 0..6) {
                        if (currentDay == 1 && i < adjustedFirstDayOfWeek) {
                            // 填充月初空白
                            Box(modifier = Modifier.weight(1f))
                        } else if (currentDay <= daysInMonth) {
                            // 显示日期
                            val date = firstDayOfMonth.plus(currentDay - 1, DateTimeUnit.DAY)
                            val isSelected = date == selectedDate.date
                            val isToday = date == Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                            
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(4.dp)
                                    .clickable { onDateSelected(date.atTime(selectedDate.hour, selectedDate.minute)) }
                                    .background(
                                        color = when {
                                            isSelected -> MaterialTheme.colorScheme.primary
                                            isToday -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                            else -> Color.Transparent
                                        },
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = currentDay.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = when {
                                        isSelected -> MaterialTheme.colorScheme.onPrimary
                                        isToday -> MaterialTheme.colorScheme.primary
                                        else -> MaterialTheme.colorScheme.onSurface
                                    }
                                )
                            }
                            currentDay++
                        } else {
                            // 填充月末空白
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DateCard(
    date: LocalDateTime,
    eventCount: Int,
    isScrolled: Boolean
) {
    val weekDayMap = mapOf(
        1 to "一",
        2 to "二",
        3 to "三",
        4 to "四",
        5 to "五",
        6 to "六",
        7 to "日"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = if (isScrolled) 8.dp else 16.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isScrolled) {
                Text(
                    text = "${date.dayOfMonth}",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "星期${weekDayMap[date.dayOfWeek.value]}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (isScrolled) Arrangement.Center else Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isScrolled) {
                    Text(
                        text = "${date.monthNumber}月${date.dayOfMonth}日",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = if (isScrolled) "共$eventCount 个事件" else "今日有 $eventCount 个事件",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun EventList(
    events: List<Event>,
    onEventClick: (Event) -> Unit,
    scrollState: LazyListState
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 80.dp)
        ) {
            items(events.size) { index ->
                val event = events[index]
                val isFirst = index == 0
                val isLast = index == events.size - 1
                val colorIndex = index % 5 // 使用5种不同的颜色循环

                EventCard(
                    event = event,
                    onClick = { onEventClick(event) },
                    isFirst = isFirst,
                    isLast = isLast,
                    nextEvent = if (!isLast) events[index + 1] else null,
                    colorIndex = colorIndex
                )
            }
        }
    }
}

@Composable
fun EventCard(
    event: Event,
    onClick: () -> Unit,
    isFirst: Boolean,
    isLast: Boolean,
    nextEvent: Event?,
    colorIndex: Int
) {
    // 定义5种不同的主题色
    val themeColors = listOf(
        Color(0xFF4CAF50), // 绿色
        Color(0xFF2196F3), // 蓝色
        Color(0xFF9C27B0), // 紫色
        Color(0xFFFF9800), // 橙色
        Color(0xFFE91E63)  // 粉色
    )

    val baseColor = themeColors[colorIndex]
    val color = baseColor.copy(alpha = 0.8f)
    val backgroundColor = baseColor.copy(alpha = 0.1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 时间线
            Column(
                modifier = Modifier
                    .width(60.dp)
                    .padding(end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!isFirst) {
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(16.dp)
                            .background(color)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(color)
                )

                if (!isLast) {
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(16.dp)
                            .background(color)
                    )
                }
            }

            // 事件内容
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = color
                    )
                    Text(
                        text = "${event.startTime.hour}:${event.startTime.minute} - ${event.endTime.hour}:${event.endTime.minute}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = color.copy(alpha = 0.8f)
                    )
                }

                if (event.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 2
                    )
                }

                if (!isLast && nextEvent != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(color.copy(alpha = 0.2f))
                    )
                }
            }
        }
    }
}