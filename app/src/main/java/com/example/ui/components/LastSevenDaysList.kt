package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonGreenGlow
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonYellow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.DayItemState

@Composable
fun LastSevenDaysList(
    days: List<DayItemState>,
    allPast30Days: List<DayItemState>,
    onDayClick: (DayItemState) -> Unit,
    modifier: Modifier = Modifier
) {
    val completedCount = days.count { it.isStudied }
    var show30DayGrid by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(DarkSurface)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF2B364E),
                        Color(0xFF141C28)
                    )
                ),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(18.dp)
            .testTag("last_seven_days_container")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ÚLTIMOS 7 DÍAS",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            // Completion badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurfaceVariant)
                    .border(1.dp, if (completedCount >= 5) NeonGreen.copy(alpha = 0.5f) else NeonCyan.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "$completedCount / 7 días",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (completedCount >= 5) NeonGreen else NeonCyan,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 7 Days Interactive Strip
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            days.forEach { dayState ->
                DayPill(
                    dayState = dayState,
                    onClick = { onDayClick(dayState) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Toggle Expand 30-Day Heatmap
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0F1420))
                .clickable { show30DayGrid = !show30DayGrid }
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.GridOn,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (show30DayGrid) "Ocultar historial de 30 días" else "Ver historial completo (30 días)",
                    style = MaterialTheme.typography.labelSmall,
                    color = NeonCyan,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Icon(
                imageVector = if (show30DayGrid) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = NeonCyan,
                modifier = Modifier.size(18.dp)
            )
        }

        // Expandable 30-Day Grid
        AnimatedVisibility(
            visible = show30DayGrid,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = "MATRIZ DE CONSISTENCIA (ÚLTIMOS 30 DÍAS)",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // 30 Days in 5 columns of 6 or 6 columns of 5
                val chunks = allPast30Days.chunked(6)
                chunks.forEach { rowDays ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        rowDays.forEach { d ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        when {
                                            d.isStudied -> NeonGreen.copy(alpha = 0.85f)
                                            d.isToday -> Color(0xFF1B2940)
                                            else -> Color(0xFF111724)
                                        }
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = when {
                                            d.isToday -> NeonCyan
                                            d.isStudied -> NeonGreen
                                            else -> Color(0xFF1A2234)
                                        },
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { onDayClick(d) },
                                contentAlignment = Alignment.Center
                            ) {
                                val dayNum = d.shortDate.split(" ").firstOrNull() ?: ""
                                Text(
                                    text = dayNum,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 11.sp,
                                    fontWeight = if (d.isStudied || d.isToday) FontWeight.Bold else FontWeight.Normal,
                                    color = if (d.isStudied) Color.Black else if (d.isToday) NeonCyan else TextMuted
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayPill(
    dayState: DayItemState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "today_pulse")
    val todayPulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "today_pulse"
    )

    val pillBorderColor by animateColorAsState(
        targetValue = when {
            dayState.isToday && dayState.isStudied -> NeonGreen
            dayState.isToday -> NeonCyan.copy(alpha = todayPulse)
            dayState.isStudied -> NeonGreen.copy(alpha = 0.7f)
            else -> Color(0xFF1D273B)
        },
        label = "pill_border"
    )

    val pillBackground = when {
        dayState.isToday -> Color(0xFF162030)
        dayState.isStudied -> Color(0xFF0F221B)
        else -> Color(0xFF0C111C)
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(pillBackground)
            .border(
                width = if (dayState.isToday) 1.8.dp else 1.dp,
                color = pillBorderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 2.dp)
            .testTag("day_pill_${dayState.dateStr}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Day Label (e.g. HOY, AYER, LUN)
        Text(
            text = dayState.dayName,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 9.sp,
            fontWeight = if (dayState.isToday) FontWeight.Black else FontWeight.Bold,
            color = if (dayState.isToday) NeonCyan else TextMuted,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Day Number
        val dayNumber = dayState.shortDate.split(" ").firstOrNull() ?: ""
        Text(
            text = dayNumber,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Black,
            color = if (dayState.isToday) TextPrimary else TextSecondary,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Status circle indicator
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(
                    if (dayState.isStudied) NeonGreen else Color(0xFF182236)
                )
                .border(
                    width = 1.dp,
                    color = if (dayState.isStudied) NeonGreen else Color(0xFF28364F),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (dayState.isStudied) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Estudiado",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(if (dayState.isToday) NeonCyan else TextMuted)
                )
            }
        }
    }
}
