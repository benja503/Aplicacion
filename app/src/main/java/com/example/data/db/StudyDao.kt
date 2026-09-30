package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.StudyDayRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDao {
    @Query("SELECT * FROM study_days ORDER BY dateStr DESC")
    fun getAllDays(): Flow<List<StudyDayRecord>>

    @Query("SELECT * FROM study_days WHERE dateStr = :dateStr LIMIT 1")
    suspend fun getDayByDate(dateStr: String): StudyDayRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDay(record: StudyDayRecord)

    @Query("DELETE FROM study_days WHERE dateStr = :dateStr")
    suspend fun deleteDay(dateStr: String)

    @Query("SELECT COUNT(*) FROM study_days")
    fun getTotalDaysStudied(): Flow<Int>
}
