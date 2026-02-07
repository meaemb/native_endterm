package com.example.campuscompanion.domain.model

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val time: String,
    val imageUrl: String?,
    val isLocal: Boolean = false,
    val updatedAt: Long = 0L,
    val sourceQuery: String? = null // null = feed, not null = search cache
)
