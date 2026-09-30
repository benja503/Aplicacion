package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_days")
data class StudyDayRecord(
    @PrimaryKey
    val dateStr: String, // Format: "yyyy-MM-dd"
    val timestamp: Long = System.currentTimeMillis()
)
