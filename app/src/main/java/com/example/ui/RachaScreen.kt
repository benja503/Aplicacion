package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.components.BigStudyButton
import com.example.ui.components.CelebrationBanner
import com.example.ui.components.DayDetailDialog
import com.example.ui.components.LastSevenDaysList
import com.example.ui.components.MotivationCard
import com.example.ui.components.NeonParticleBurst
import com.example.ui.components.QuickStudyTimer
import com.example.ui.components.StreakDisplay
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonYellow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.viewmodel.RachaViewModel

@Composable
fun RachaScreen(
    viewModel: RachaViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val safeInsets = WindowInsets.safeDrawing.asPaddingValues()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .drawBehind {
                    // Atmospheric cyberpunk glowing gradients
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0x2200F0FF),
                                Color.Transparent
                            ),
                            center = Offset(size.width * 0.5f, size.height * 0.18f),
                            radius = size.width * 0.85f
                        )
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0x18FF5E00),
                                Color.Transparent
                            ),
                            center = Offset(size.width * 0.5f, size.height * 0.48f),
                            radius = size.width * 0.75f
                        )
                    )
                },
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 640.dp)
                    .padding(
                        top = safeInsets.calculateTopPadding(),
                        bottom = safeInsets.calculateBottomPadding()
                    )
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top App Bar / Brand Header
                AppHeader()

                Spacer(modifier = Modifier.height(10.dp))

                // Hero Graphic Banner
                HeroBanner()

                Spacer(modifier = Modifier.height(14.dp))

                // Celebration banner if triggered
                CelebrationBanner(
                    message = uiState.celebrationMessage,
                    onDismiss = { viewModel.clearCelebrationMessage() },
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // 1. Contador grande de días consecutivos con indicador circular
                StreakDisplay(
                    streakCount = uiState.streakCount,
                    isStudiedToday = uiState.isStudiedToday,
                    bestStreak = uiState.bestStreak,
                    totalDaysStudied = uiState.totalDaysStudied,
                    milestone = uiState.milestone,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 2. Botón gigante «Hoy sí estudié»
                BigStudyButton(
                    isStudiedToday = uiState.isStudiedToday,
                    onStudyClick = { viewModel.onStudyTodayClicked() },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 3. Temporizador de enfoque rápido (Pomodoro / Estudio)
                QuickStudyTimer(
                    isRunning = uiState.timerRunning,
                    secondsLeft = uiState.timerSecondsLeft,
                    totalSeconds = uiState.timerTotalSeconds,
                    onStart = { viewModel.startTimer() },
                    onPause = { viewModel.pauseTimer() },
                    onReset = { viewModel.resetTimer() },
                    onPresetSelect = { minutes -> viewModel.setTimerPreset(minutes) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 4. Lista de los últimos siete días con matriz de 30 días
                LastSevenDaysList(
                    days = uiState.last7Days,
                    allPast30Days = uiState.last30Days,
                    onDayClick = { day -> viewModel.onSelectDayForDetail(day) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 5. Mensaje motivador distinto cada vez
                MotivationCard(
                    quote = uiState.currentQuote,
                    onRefreshQuote = { viewModel.onRefreshQuote() },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Privacy / Offline Footer
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "100% Offline · Tus datos no salen de tu teléfono · Sin anuncios",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Confetti / Neon Particle Burst over whole screen
            NeonParticleBurst(trigger = uiState.celebrationTrigger)

            // Day Detail Dialog
            uiState.selectedDayForDetail?.let { day ->
                DayDetailDialog(
                    day = day,
                    onDismiss = { viewModel.onSelectDayForDetail(null) },
                    onToggle = { dateStr -> viewModel.onToggleDay(dateStr) }
                )
            }
        }
    }
}

@Composable
private fun HeroBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(DarkSurfaceVariant)
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        NeonCyan.copy(alpha = 0.5f),
                        NeonOrange.copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(22.dp)
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.neon_racha_banner),
            contentDescription = "RACHA Banner",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay for seamless text integration
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            DarkBackground.copy(alpha = 0.85f),
                            Color.Transparent,
                            DarkBackground.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "FORJA TU DISCIPLINA",
                    style = MaterialTheme.typography.labelSmall,
                    color = NeonYellow,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "El hábito supera al talento.",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .border(1.dp, NeonCyan.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "MODO IMPARABLE",
                    style = MaterialTheme.typography.labelSmall,
                    color = NeonCyan,
                    fontWeight = FontWeight.Black,
                    fontSize = 9.sp
                )
            }
        }
    }
}

@Composable
private fun AppHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(NeonOrange, NeonCyan)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = "Racha logo",
                    tint = DarkBackground,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "RACHA",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 3.sp
                    ),
                    color = TextPrimary,
                    modifier = Modifier.testTag("app_title")
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(DarkSurfaceVariant)
                .border(1.dp, Color(0xFF243044), RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp, vertical = 5.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(com.example.ui.theme.NeonGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "OFFLINE · ACTIVO",
                    style = MaterialTheme.typography.labelSmall,
                    color = NeonCyan,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
