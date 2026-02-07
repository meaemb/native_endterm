package com.example.campuscompanion.domain.repository

import com.example.campuscompanion.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventsRepository {
    fun observeFeed(): Flow<List<Event>>
    fun observeSearch(query: String): Flow<List<Event>>
    suspend fun refresh(query: String?, page: Int, pageSize: Int, reset: Boolean): Int

    suspend fun getById(id: String): Event?
    suspend fun upsert(event: Event)
    suspend fun delete(id: String)
}
