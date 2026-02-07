package com.example.campuscompanion

import com.example.campuscompanion.domain.model.Event
import com.example.campuscompanion.domain.repository.Comment
import com.example.campuscompanion.domain.repository.EventsRepository
import com.example.campuscompanion.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeEventsRepo : EventsRepository {

    val feed = MutableStateFlow<List<Event>>(emptyList())
    var shouldFailRefresh = false

    override fun observeFeed(): Flow<List<Event>> = feed
    override fun observeSearch(query: String): Flow<List<Event>> = flowOf(emptyList())

    override suspend fun refresh(
        query: String?,
        page: Int,
        pageSize: Int,
        reset: Boolean
    ): Int {
        if (shouldFailRefresh) throw RuntimeException("Network error")
        return 0
    }

    override suspend fun getById(id: String): Event? =
        feed.value.firstOrNull { it.id == id }

    override suspend fun upsert(event: Event) {
        feed.value = feed.value + event
    }

    override suspend fun delete(id: String) {}
}

class FakeUserRepo : UserRepository {

    var uid: String? = null
    var email: String? = null

    override fun currentUserId(): String? = uid
    override fun currentUserEmail(): String? = email

    override suspend fun signIn(email: String, password: String) {
        uid = "u1"
        this.email = email
    }

    override suspend fun signUp(email: String, password: String) {
        uid = "u1"
        this.email = email
    }

    override suspend fun signOut() {
        uid = null
        email = null
    }

    override suspend fun toggleFavorite(eventId: String) {
        if (uid == null) throw IllegalStateException("Not logged in")
    }

    override suspend fun getFavoriteIds(): Set<String> = emptySet()

    override fun observeComments(eventId: String): Flow<List<Comment>> =
        flowOf(emptyList())

    override suspend fun addComment(eventId: String, text: String) {}
    override suspend fun deleteComment(eventId: String, commentId: String) {}
}
