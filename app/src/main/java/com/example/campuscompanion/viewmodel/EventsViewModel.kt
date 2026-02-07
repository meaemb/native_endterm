package com.example.campuscompanion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuscompanion.domain.model.Event
import com.example.campuscompanion.domain.repository.Comment
import com.example.campuscompanion.domain.repository.EventsRepository
import com.example.campuscompanion.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventsRepo: EventsRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    // UI
    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> = _uiMessage.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    fun clearUiMessage() { _uiMessage.value = null }

    // Auth
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email.asStateFlow()

    // Search query
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()
    fun setQuery(value: String) { _query.value = value }

    // Pagination state
    private val pageSize = 20
    private var feedPage = 1
    private var feedCanLoadMore = true
    private var searchPage = 1
    private var searchCanLoadMore = true

    // Selected event id
    private val _selectedId = MutableStateFlow<String?>(null)
    fun selectEvent(id: String) { _selectedId.value = id }

    // Streams from Room
    val feedEvents: StateFlow<List<Event>> =
        eventsRepo.observeFeed()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val currentSearchQuery = MutableStateFlow("")
    val searchEvents: StateFlow<List<Event>> =
        currentSearchQuery
            .flatMapLatest { q ->
                if (q.isBlank()) flowOf(emptyList())
                else eventsRepo.observeSearch(q)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedEvent: StateFlow<Event?> =
        _selectedId
            .flatMapLatest { id ->
                if (id == null) {
                    flowOf(null)
                } else {
                    flowOf(eventsRepo.getById(id))
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null
            )

    // Favorites ids
    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    val favoriteEvents: StateFlow<List<Event>> =
        combine(feedEvents, favorites) { all, favIds ->
            all.filter { favIds.contains(it.id) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Realtime comments
    val comments: StateFlow<List<Comment>> =
        _selectedId
            .flatMapLatest { id ->
                if (id == null) flowOf(emptyList())
                else userRepo.observeComments(id)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        _isLoggedIn.value = userRepo.currentUserId() != null
        _email.value = userRepo.currentUserEmail()

        // Debounced search
        viewModelScope.launch {
            query
                .debounce(400)
                .map { it.trim() }
                .distinctUntilChanged()
                .collectLatest { q ->
                    startNewSearch(q)
                }
        }

        // Initial refresh WITHOUT error message
        refreshFeed(showError = false)

        if (_isLoggedIn.value) refreshFavorites()
    }


    // Auth actions
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _uiMessage.value = null
            try {
                userRepo.signIn(email.trim(), password)
                _isLoggedIn.value = true
                _email.value = userRepo.currentUserEmail()
                _uiMessage.value = "Login successful"
                refreshFavorites()
            } catch (e: Exception) {
                _uiMessage.value = mapAuthError(e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _uiMessage.value = null
            try {
                userRepo.signUp(email.trim(), password)
                _isLoggedIn.value = true
                _email.value = userRepo.currentUserEmail()
                _uiMessage.value = "Registration successful"
                refreshFavorites()
            } catch (e: Exception) {
                _uiMessage.value = mapAuthError(e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            userRepo.signOut()
            _isLoggedIn.value = false
            _email.value = null
            _favorites.value = emptySet()
            _uiMessage.value = "Signed out"
        }
    }

    private fun mapAuthError(e: Exception): String {
        val code = (e as? FirebaseAuthException)?.errorCode
        return when (code) {
            "ERROR_INVALID_CREDENTIAL", "ERROR_WRONG_PASSWORD" -> "Wrong email or password"
            "ERROR_USER_NOT_FOUND" -> "Account does not exist. Please register first."
            "ERROR_EMAIL_ALREADY_IN_USE" -> "You are already registered. Please sign in."
            "ERROR_WEAK_PASSWORD" -> "Password is too weak (min 6 characters)."
            "ERROR_INVALID_EMAIL" -> "Invalid email format."
            else -> e.message ?: "Authentication error"
        }
    }

    private fun requireLoginMessage() {
        _uiMessage.value = "Please sign in or register to use this feature."
    }

    // Feed refresh / pagination
    fun refreshFeed(showError: Boolean = true) {
        viewModelScope.launch {
            _loading.value = true
            try {
                feedPage = 1
                feedCanLoadMore = true
                val count = eventsRepo.refresh(
                    query = null,
                    page = 1,
                    pageSize = pageSize,
                    reset = true
                )
                feedCanLoadMore = count == pageSize
            } catch (_: Exception) {
                if (showError) {
                    _uiMessage.value = "Network error. Showing cached data."
                }
            } finally {
                _loading.value = false
            }
        }
    }


    fun loadMoreFeed() {
        if (!feedCanLoadMore || _loading.value) return
        viewModelScope.launch {
            _loading.value = true
            try {
                val next = feedPage + 1
                val count = eventsRepo.refresh(query = null, page = next, pageSize = pageSize, reset = false)
                if (count > 0) feedPage = next
                feedCanLoadMore = count == pageSize
            } catch (_: Exception) {
                _uiMessage.value = "Failed to load more. Check internet."
            } finally {
                _loading.value = false
            }
        }
    }

    // Search refresh / pagination
    private fun startNewSearch(q: String) {
        currentSearchQuery.value = q
        if (q.isBlank()) return

        viewModelScope.launch {
            _loading.value = true
            try {
                searchPage = 1
                searchCanLoadMore = true
                val count = eventsRepo.refresh(query = q, page = 1, pageSize = pageSize, reset = true)
                searchCanLoadMore = count == pageSize
            } catch (_: Exception) {
                _uiMessage.value = "Search failed. Showing cached results (if any)."
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadMoreSearch() {
        val q = currentSearchQuery.value
        if (q.isBlank() || !searchCanLoadMore || _loading.value) return

        viewModelScope.launch {
            _loading.value = true
            try {
                val next = searchPage + 1
                val count = eventsRepo.refresh(query = q, page = next, pageSize = pageSize, reset = false)
                if (count > 0) searchPage = next
                searchCanLoadMore = count == pageSize
            } catch (_: Exception) {
                _uiMessage.value = "Failed to load more search results."
            } finally {
                _loading.value = false
            }
        }
    }

    // CRUD local events
    fun createEvent(title: String, description: String, location: String, time: String, imageUrl: String?) {
        if (!_isLoggedIn.value) { requireLoginMessage(); return }
        if (title.trim().isBlank()) { _uiMessage.value = "Title is required"; return }

        viewModelScope.launch {
            val id = UUID.randomUUID().toString()
            eventsRepo.upsert(
                Event(
                    id = id,
                    title = title.trim(),
                    description = description.trim(),
                    location = location.trim(),
                    time = time.trim(),
                    imageUrl = imageUrl?.trim(),
                    isLocal = true,
                    updatedAt = System.currentTimeMillis(),
                    sourceQuery = null
                )
            )
            _uiMessage.value = "Event created"
        }
    }

    fun updateEvent(id: String, title: String, description: String, location: String, time: String, imageUrl: String?) {
        if (!_isLoggedIn.value) { requireLoginMessage(); return }
        if (title.trim().isBlank()) { _uiMessage.value = "Title is required"; return }

        viewModelScope.launch {
            eventsRepo.upsert(
                Event(
                    id = id,
                    title = title.trim(),
                    description = description.trim(),
                    location = location.trim(),
                    time = time.trim(),
                    imageUrl = imageUrl?.trim(),
                    isLocal = true,
                    updatedAt = System.currentTimeMillis(),
                    sourceQuery = null
                )
            )
            _uiMessage.value = "Event updated"
        }
    }

    fun deleteEvent(id: String) {
        if (!_isLoggedIn.value) { requireLoginMessage(); return }
        viewModelScope.launch {
            eventsRepo.delete(id)
            _uiMessage.value = "Event deleted"
        }
    }

    // Favorites
    fun refreshFavorites() {
        viewModelScope.launch {
            _favorites.value = userRepo.getFavoriteIds()
        }
    }

    fun toggleFavorite(id: String) {
        if (!_isLoggedIn.value) { requireLoginMessage(); return }
        viewModelScope.launch {
            runCatching { userRepo.toggleFavorite(id) }
                .onFailure { _uiMessage.value = it.message ?: "Failed to update favorites" }
            _favorites.value = userRepo.getFavoriteIds()
        }
    }

    // Comments
    fun addComment(text: String) {
        if (!_isLoggedIn.value) { requireLoginMessage(); return }
        val eventId = _selectedId.value ?: return
        val t = text.trim()
        if (t.isBlank()) return

        viewModelScope.launch {
            runCatching { userRepo.addComment(eventId, t) }
                .onFailure { _uiMessage.value = it.message ?: "Failed to add comment" }
        }
    }
}
