package com.example.campuscompanion.domain.repository

data class Comment(
    val id: String,
    val userId: String,
    val userEmail: String,
    val text: String,
    val timestamp: Long
)
