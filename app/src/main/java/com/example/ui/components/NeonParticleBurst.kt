package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonFlame
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonYellow
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class Particle(
    val angle: Double,
    val speed: Float,
    val size: Float,
    val color: Color
)

@Composable
fun NeonParticleBurst(
    trigger: Long,
    modifier: Modifier = Modifier
) {
    if (trigger == 0L) return

    val progress = remember(trigger) { Animatable(0f) }
    val particleColors = remember {
        listOf(NeonOrange, NeonFlame, NeonCyan, NeonGreen, NeonYellow)
    }

    val particles = remember(trigger) {
        List(40) {
            val angle = Random.nextDouble(0.0, Math.PI * 2)
            val speed = Random.nextFloat() * 450f + 150f
            val size = Random.nextFloat() * 6f + 3f
            val color = particleColors[Random.nextInt(particleColors.size)]
            Particle(angle, speed, size, color)
        }
    }

    LaunchedEffect(trigger) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1100)
        )
    }

    if (progress.value in 0.001f..0.999f) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val centerX = size.width / 2f
            val centerY = size.height * 0.42f
            val p = progress.value
            val alpha = (1f - p).coerceIn(0f, 1f)

            particles.forEach { particle ->
                val distance = particle.speed * p
                val gravity = 300f * p * p
                val x = centerX + (distance * cos(particle.angle)).toFloat()
                val y = centerY + (distance * sin(particle.angle)).toFloat() + gravity

                drawCircle(
                    color = particle.color.copy(alpha = alpha),
                    radius = particle.size * (1f - p * 0.3f),
                    center = Offset(x, y)
                )
            }
        }
    }
}
