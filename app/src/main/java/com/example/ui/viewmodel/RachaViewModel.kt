package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.MotivationQuote
import com.example.data.MotivationQuotesProvider
import com.example.data.repository.StudyRepository
import com.example.util.DateUtils
import com.example.util.StreakMilestone
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DayItemState(
    val dateStr: String,
    val dayName: String,
    val shortDate: String,
    val isStudied: Boolean,
    val isToday: Boolean,
    val daysAgo: Int
)

data class RachaUiState(
    val streakCount: Int = 0,
    val isStudiedToday: Boolean = false,
    val last7Days: List<DayItemState> = emptyList(),
    val last30Days: List<DayItemState> = emptyList(),
    val currentQuote: MotivationQuote = MotivationQuotesProvider.quotes.first(),
    val currentQuoteIndex: Int = 0,
    val totalDaysStudied: Int = 0,
    val bestStreak: Int = 0,
    val milestone: StreakMilestone = DateUtils.getMilestone(0),
    val celebrationTrigger: Long = 0L,
    val celebrationMessage: String? = null,
    val selectedDayForDetail: DayItemState? = null,
    // Quick study timer state
    val timerRunning: Boolean = false,
    val timerSecondsLeft: Int = 25 * 60,
    val timerTotalSeconds: Int = 25 * 60,
    val timerFinished: Boolean = false
)

private data class RachaSessionState(
    val currentQuote: MotivationQuote,
    val currentQuoteIndex: Int,
    val celebrationTrigger: Long = 0L,
    val celebrationMessage: String? = null,
    val selectedDayForDetail: DayItemState? = null,
    val timerRunning: Boolean = false,
    val timerSecondsLeft: Int = 25 * 60,
    val timerTotalSeconds: Int = 25 * 60,
    val timerFinished: Boolean = false
)

class RachaViewModel(private val repository: StudyRepository) : ViewModel() {

    private val initialQuotePair = MotivationQuotesProvider.getRandomQuote()

    private val _sessionState = MutableStateFlow(
        RachaSessionState(
            currentQuote = initialQuotePair.second,
            currentQuoteIndex = initialQuotePair.first
        )
    )

    private var timerJob: Job? = null

    val uiState: StateFlow<RachaUiState> = combine(
        repository.allDays,
        _sessionState
    ) { records, session ->
        val studiedDates = records.map { it.dateStr }.toSet()
        val todayStr = DateUtils.getTodayString()
        val hasStudiedToday = studiedDates.contains(todayStr)
        val streak = DateUtils.calculateCurrentStreak(studiedDates)
        val best = DateUtils.calculateBestStreak(studiedDates)
        val milestone = DateUtils.getMilestone(streak)

        // Build list of last 7 days (from 6 days ago up to today, 0)
        val last7 = (6 downTo 0).map { daysAgo ->
            val dateStr = DateUtils.getDateStringForDaysAgo(daysAgo)
            val dayName = DateUtils.getDayLabel(daysAgo)
            val shortDate = DateUtils.getFormattedShortDate(daysAgo)
            DayItemState(
                dateStr = dateStr,
                dayName = dayName,
                shortDate = shortDate,
                isStudied = studiedDates.contains(dateStr),
                isToday = daysAgo == 0,
                daysAgo = daysAgo
            )
        }

        // Build list of last 30 days for habit heatmap
        val last30 = (29 downTo 0).map { daysAgo ->
            val dateStr = DateUtils.getDateStringForDaysAgo(daysAgo)
            val dayName = DateUtils.getDayLabel(daysAgo)
            val shortDate = DateUtils.getFormattedShortDate(daysAgo)
            DayItemState(
                dateStr = dateStr,
                dayName = dayName,
                shortDate = shortDate,
                isStudied = studiedDates.contains(dateStr),
                isToday = daysAgo == 0,
                daysAgo = daysAgo
            )
        }

        // Update selected day if active
        val refreshedSelectedDay = session.selectedDayForDetail?.let { current ->
            last30.find { it.dateStr == current.dateStr }
        }

        RachaUiState(
            streakCount = streak,
            isStudiedToday = hasStudiedToday,
            last7Days = last7,
            last30Days = last30,
            currentQuote = session.currentQuote,
            currentQuoteIndex = session.currentQuoteIndex,
            totalDaysStudied = studiedDates.size,
            bestStreak = best,
            milestone = milestone,
            celebrationTrigger = session.celebrationTrigger,
            celebrationMessage = session.celebrationMessage,
            selectedDayForDetail = refreshedSelectedDay,
            timerRunning = session.timerRunning,
            timerSecondsLeft = session.timerSecondsLeft,
            timerTotalSeconds = session.timerTotalSeconds,
            timerFinished = session.timerFinished
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RachaUiState()
    )

    fun onStudyTodayClicked() {
        viewModelScope.launch {
            val currentState = uiState.value
            val (nextIdx, nextQuote) = MotivationQuotesProvider.getRandomQuote(currentState.currentQuoteIndex)

            if (!currentState.isStudiedToday) {
                repository.markTodayStudied()
                _sessionState.update {
                    it.copy(
                        currentQuote = nextQuote,
                        currentQuoteIndex = nextIdx,
                        celebrationTrigger = System.currentTimeMillis(),
                        celebrationMessage = "¡+1 DÍA! Racha encendida a toda potencia 🔥"
                    )
                }
            } else {
                _sessionState.update {
                    it.copy(
                        currentQuote = nextQuote,
                        currentQuoteIndex = nextIdx,
                        celebrationTrigger = System.currentTimeMillis(),
                        celebrationMessage = "¡Tu racha sigue activa hoy! Nueva frase lista ⚡"
                    )
                }
            }
        }
    }

    fun onToggleDay(dateStr: String) {
        viewModelScope.launch {
            repository.toggleDay(dateStr)
        }
    }

    fun onSelectDayForDetail(day: DayItemState?) {
        _sessionState.update { it.copy(selectedDayForDetail = day) }
    }

    fun onRefreshQuote() {
        val (nextIdx, nextQuote) = MotivationQuotesProvider.getRandomQuote(_sessionState.value.currentQuoteIndex)
        _sessionState.update {
            it.copy(
                currentQuote = nextQuote,
                currentQuoteIndex = nextIdx
            )
        }
    }

    fun clearCelebrationMessage() {
        _sessionState.update { it.copy(celebrationMessage = null) }
    }

    // Focus Timer Controls
    fun startTimer() {
        if (_sessionState.value.timerRunning) return
        _sessionState.update { it.copy(timerRunning = true, timerFinished = false) }
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_sessionState.value.timerRunning && _sessionState.value.timerSecondsLeft > 0) {
                delay(1000)
                _sessionState.update { it.copy(timerSecondsLeft = it.timerSecondsLeft - 1) }
            }
            if (_sessionState.value.timerSecondsLeft <= 0) {
                _sessionState.update {
                    it.copy(
                        timerRunning = false,
                        timerFinished = true,
                        celebrationMessage = "¡Sesión de estudio completada con éxito! 🧠⚡",
                        celebrationTrigger = System.currentTimeMillis()
                    )
                }
                if (!uiState.value.isStudiedToday) {
                    repository.markTodayStudied()
                }
            }
        }
    }

    fun pauseTimer() {
        _sessionState.update { it.copy(timerRunning = false) }
        timerJob?.cancel()
    }

    fun resetTimer() {
        timerJob?.cancel()
        _sessionState.update {
            it.copy(
                timerRunning = false,
                timerSecondsLeft = it.timerTotalSeconds,
                timerFinished = false
            )
        }
    }

    fun setTimerPreset(minutes: Int) {
        timerJob?.cancel()
        val totalSecs = minutes * 60
        _sessionState.update {
            it.copy(
                timerRunning = false,
                timerTotalSeconds = totalSecs,
                timerSecondsLeft = totalSecs,
                timerFinished = false
            )
        }
    }

    companion object {
        fun provideFactory(repository: StudyRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RachaViewModel(repository) as T
                }
            }
    }
}
