package com.example.data.repository

import com.example.data.db.StudyDao
import com.example.data.model.StudyDayRecord
import com.example.util.DateUtils
import kotlinx.coroutines.flow.Flow

class StudyRepository(private val studyDao: StudyDao) {
    val allDays: Flow<List<StudyDayRecord>> = studyDao.getAllDays()
    val totalDays: Flow<Int> = studyDao.getTotalDaysStudied()

    suspend fun markTodayStudied() {
        val today = DateUtils.getTodayString()
        studyDao.insertDay(StudyDayRecord(dateStr = today))
    }

    suspend fun toggleDay(dateStr: String) {
        val existing = studyDao.getDayByDate(dateStr)
        if (existing != null) {
            studyDao.deleteDay(dateStr)
        } else {
            studyDao.insertDay(StudyDayRecord(dateStr = dateStr))
        }
    }

    suspend fun removeStudyDay(dateStr: String) {
        studyDao.deleteDay(dateStr)
    }

    suspend fun recordStudyDate(dateStr: String) {
        studyDao.insertDay(StudyDayRecord(dateStr = dateStr))
    }
}
