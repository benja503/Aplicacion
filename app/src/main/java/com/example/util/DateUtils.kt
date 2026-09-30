package com.example.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class StreakMilestone(
    val title: String,
    val nextTarget: Int,
    val progress: Float,
    val levelNumber: Int
)

object DateUtils {
    private const val DATE_PATTERN = "yyyy-MM-dd"

    fun getTodayString(): String {
        val sdf = SimpleDateFormat(DATE_PATTERN, Locale.US)
        return sdf.format(Date())
    }

    fun getDateStringForDaysAgo(daysAgo: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        val sdf = SimpleDateFormat(DATE_PATTERN, Locale.US)
        return sdf.format(calendar.time)
    }

    fun getDayLabel(daysAgo: Int, calendar: Calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -daysAgo) }): String {
        return when (daysAgo) {
            0 -> "HOY"
            1 -> "AYER"
            else -> {
                val dayOfWeekFormat = SimpleDateFormat("EEE", Locale("es", "ES"))
                dayOfWeekFormat.format(calendar.time).uppercase(Locale("es", "ES")).replace(".", "")
            }
        }
    }

    fun getFormattedShortDate(daysAgo: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        val monthDayFormat = SimpleDateFormat("d MMM", Locale("es", "ES"))
        return monthDayFormat.format(calendar.time)
    }

    fun getFormattedFullDate(dateStr: String): String {
        val sdf = SimpleDateFormat(DATE_PATTERN, Locale.US)
        val date = sdf.parse(dateStr) ?: return dateStr
        val fullFormat = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "ES"))
        return fullFormat.format(date).replaceFirstChar { it.uppercase(Locale("es", "ES")) }
    }

    fun calculateCurrentStreak(studiedDateStrings: Set<String>): Int {
        val todayStr = getTodayString()
        val yesterdayStr = getDateStringForDaysAgo(1)

        val hasToday = studiedDateStrings.contains(todayStr)
        val hasYesterday = studiedDateStrings.contains(yesterdayStr)

        if (!hasToday && !hasYesterday) {
            return 0
        }

        var streak = 0
        val startOffset = if (hasToday) 0 else 1

        var dayOffset = startOffset
        while (true) {
            val checkDate = getDateStringForDaysAgo(dayOffset)
            if (studiedDateStrings.contains(checkDate)) {
                streak++
                dayOffset++
            } else {
                break
            }
        }
        return streak
    }

    fun calculateBestStreak(studiedDateStrings: Set<String>): Int {
        if (studiedDateStrings.isEmpty()) return 0
        val sortedDates = studiedDateStrings.sorted()
        val sdf = SimpleDateFormat(DATE_PATTERN, Locale.US)

        var best = 1
        var current = 1

        for (i in 1 until sortedDates.size) {
            val prevDate = sdf.parse(sortedDates[i - 1]) ?: continue
            val currDate = sdf.parse(sortedDates[i]) ?: continue

            val diffMillis = currDate.time - prevDate.time
            val diffDays = (diffMillis / (1000 * 60 * 60 * 24)).toInt()

            if (diffDays == 1) {
                current++
                if (current > best) {
                    best = current
                }
            } else if (diffDays > 1) {
                current = 1
            }
        }

        return maxOf(best, calculateCurrentStreak(studiedDateStrings))
    }

    fun getMilestone(streak: Int): StreakMilestone {
        return when {
            streak == 0 -> StreakMilestone("Chispa Inicial", 3, 0f, 1)
            streak < 3 -> StreakMilestone("Chispa Inicial", 3, streak / 3f, 1)
            streak < 7 -> StreakMilestone("Llama Firme", 7, (streak - 3) / 4f, 2)
            streak < 14 -> StreakMilestone("Antorcha de Acero", 14, (streak - 7) / 7f, 3)
            streak < 21 -> StreakMilestone("Hoguera Imparable", 21, (streak - 14) / 7f, 4)
            streak < 30 -> StreakMilestone("Rayo de Enfoque", 30, (streak - 21) / 9f, 5)
            streak < 60 -> StreakMilestone("Fénix Legendario", 60, (streak - 30) / 30f, 6)
            streak < 100 -> StreakMilestone("Titán del Estudio", 100, (streak - 60) / 40f, 7)
            else -> StreakMilestone("Maestro Supremo", 365, minOf(streak / 365f, 1f), 8)
        }
    }
}
