package com.example.campuscompanion.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val location: String,
    val time: String,
    val imageUrl: String?,
    val isLocal: Boolean,
    val updatedAt: Long,
    val sourceQuery: String? // null = feed, not null = search cache
)
