package com.example.campuscompanion.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun currentUserId(): String?
    fun currentUserEmail(): String?

    suspend fun signIn(email: String, password: String)
    suspend fun signUp(email: String, password: String)
    suspend fun signOut()

    suspend fun toggleFavorite(eventId: String)
    suspend fun getFavoriteIds(): Set<String>

    fun observeComments(eventId: String): Flow<List<Comment>>
    suspend fun addComment(eventId: String, text: String)
    suspend fun deleteComment(eventId: String, commentId: String)
}
