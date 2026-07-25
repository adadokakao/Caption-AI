package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_captions")
data class CaptionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val fullText: String,
    val hook: String,
    val platform: String,
    val style: String,
    val language: String,
    val hashtagsJson: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)
