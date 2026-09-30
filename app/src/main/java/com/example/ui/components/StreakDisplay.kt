package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonFlame
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonOrangeGlow
import com.example.ui.theme.NeonYellow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.StreakMilestone

@Composable
fun StreakDisplay(
    streakCount: Int,
    isStudiedToday: Boolean,
    bestStreak: Int,
    totalDaysStudied: Int,
    milestone: StreakMilestone,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "streak_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (streakCount > 0) 1.08f else 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val haloAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.65f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo_alpha"
    )

    val streakActive = streakCount > 0
    val flameColor = when {
        streakCount >= 7 -> NeonOrange
        streakCount >= 3 -> NeonFlame
        streakCount >= 1 -> NeonYellow
        else -> TextMuted
    }

    val animatedProgress by animateFloatAsState(
        targetValue = milestone.progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 800),
        label = "milestone_progress"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DarkSurfaceVariant.copy(alpha = 0.9f),
                        DarkSurface.copy(alpha = 0.98f)
                    )
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = if (streakActive) {
                        listOf(
                            NeonOrange.copy(alpha = 0.9f),
                            NeonCyan.copy(alpha = 0.6f),
                            Color(0xFF1F293D)
                        )
                    } else {
                        listOf(Color(0xFF2C394F), Color(0xFF151C2A))
                    }
                ),
                shape = RoundedCornerShape(32.dp)
            )
            .padding(vertical = 24.dp, horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top status pill
        val (badgeText, badgeColor, badgeIcon) = when {
            isStudiedToday -> Triple("¡RACHA BLINDADA HOY!", NeonGreen, Icons.Default.ElectricBolt)
            streakCount > 0 -> Triple("RACHA EN CURSO · ESTUDIA HOY", NeonYellow, Icons.Default.LocalFireDepartment)
            else -> Triple("INICIA TU FUEGO HOY", NeonCyan, Icons.Default.Stars)
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(badgeColor.copy(alpha = 0.12f))
                .border(1.dp, badgeColor.copy(alpha = 0.5f), RoundedCornerShape(50))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = badgeIcon,
                    contentDescription = null,
                    tint = badgeColor,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = badgeText,
                    color = badgeColor,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Big Circular Neon Gauge with Streak Counter inside
        Box(
            modifier = Modifier
                .size(200.dp)
                .testTag("streak_gauge_box"),
            contentAlignment = Alignment.Center
        ) {
            // Neon Glow Ring & Arc Canvas
            Canvas(modifier = Modifier.size(190.dp)) {
                val strokeWidth = 10.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2f
                val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
                val arcSize = Size(radius * 2f, radius * 2f)

                // Background track
                drawArc(
                    color = Color(0xFF151C2A),
                    startAngle = 135f,
                    sweepAngle = 270f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Active progress arc
                val activeSweep = 270f * animatedProgress
                if (activeSweep > 0f) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                NeonYellow,
                                NeonOrange,
                                NeonFlame,
                                NeonCyan
                            )
                        ),
                        startAngle = 135f,
                        sweepAngle = activeSweep,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                // Decorative tick marks around the gauge
                val tickCount = 18
                for (i in 0..tickCount) {
                    val angleDeg = 135f + (270f / tickCount) * i
                    val angleRad = Math.toRadians(angleDeg.toDouble())
                    val innerR = radius - 14.dp.toPx()
                    val outerR = radius - 8.dp.toPx()
                    val cx = size.width / 2f
                    val cy = size.height / 2f

                    val startX = (cx + innerR * Math.cos(angleRad)).toFloat()
                    val startY = (cy + innerR * Math.sin(angleRad)).toFloat()
                    val endX = (cx + outerR * Math.cos(angleRad)).toFloat()
                    val endY = (cy + outerR * Math.sin(angleRad)).toFloat()

                    drawCircle(
                        color = if (i.toFloat() / tickCount <= animatedProgress) NeonCyan.copy(alpha = 0.7f) else Color(0xFF263248),
                        radius = 1.5.dp.toPx(),
                        center = Offset(endX, endY)
                    )
                }
            }

            // Central Stack (Flame + Huge Number)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Pulsing Flame
                Box(
                    modifier = Modifier
                        .scale(pulseScale)
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(if (streakActive) NeonOrangeGlow.copy(alpha = haloAlpha) else Color(0xFF141A28)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Fuego",
                        tint = flameColor,
                        modifier = Modifier.size(32.dp)
                    )
                }

                AnimatedContent(
                    targetState = streakCount,
                    transitionSpec = {
                        slideInVertically { height -> height } + fadeIn() togetherWith
                                slideOutVertically { height -> -height } + fadeOut()
                    },
                    label = "streak_number_animation"
                ) { targetStreak ->
                    Text(
                        text = "$targetStreak",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = if (targetStreak >= 100) 64.sp else 80.sp,
                            lineHeight = if (targetStreak >= 100) 68.sp else 84.sp
                        ),
                        color = if (targetStreak > 0) TextPrimary else TextMuted,
                        modifier = Modifier.testTag("streak_count_text")
                    )
                }

                Text(
                    text = if (streakCount == 1) "DÍA SEGUIDO" else "DÍAS SEGUIDOS",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (streakActive) NeonCyan else TextMuted,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Level / Milestone Rank Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF0C101A))
                .border(1.dp, Color(0xFF1D2638), RoundedCornerShape(16.dp))
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = NeonYellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RANGO: ${milestone.title.uppercase()}",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonYellow,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = "Meta: ${milestone.nextTarget} días",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = NeonOrange,
                    trackColor = Color(0xFF1B2335)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stats Footer (Best Streak + Total Days)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Stat 1: Best Streak
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F1420))
                    .border(1.dp, Color(0xFF1E283A), RoundedCornerShape(16.dp))
                    .padding(vertical = 10.dp, horizontal = 12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "RÉCORD HISTÓRICO",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$bestStreak días 🔥",
                        style = MaterialTheme.typography.titleMedium,
                        color = NeonYellow,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            // Stat 2: Total Days
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F1420))
                    .border(1.dp, Color(0xFF1E283A), RoundedCornerShape(16.dp))
                    .padding(vertical = 10.dp, horizontal = 12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "DÍAS TOTALES",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$totalDaysStudied sesiones 📚",
                        style = MaterialTheme.typography.titleMedium,
                        color = NeonCyan,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}
