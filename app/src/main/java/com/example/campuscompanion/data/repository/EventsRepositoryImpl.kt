package com.example.campuscompanion.data.repository

import com.example.campuscompanion.data.local.dao.EventDao
import com.example.campuscompanion.data.mapper.EventMapper
import com.example.campuscompanion.data.remote.api.EventsApi
import com.example.campuscompanion.domain.model.Event
import com.example.campuscompanion.domain.repository.EventsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsRepositoryImpl @Inject constructor(
    private val api: EventsApi,
    private val dao: EventDao
) : EventsRepository {

    override fun observeFeed(): Flow<List<Event>> =
        dao.observeFeed().map { list -> list.map(EventMapper::entityToDomain) }

    override fun observeSearch(query: String): Flow<List<Event>> =
        dao.observeSearch(query).map { list -> list.map(EventMapper::entityToDomain) }

    override suspend fun getById(id: String): Event? =
        dao.getById(id)?.let(EventMapper::entityToDomain)

    override suspend fun upsert(event: Event) {
        dao.upsert(EventMapper.domainToEntity(event))
    }

    override suspend fun delete(id: String) {
        dao.delete(id)
    }

    override suspend fun refresh(
        query: String?,
        page: Int,
        pageSize: Int,
        reset: Boolean
    ): Int {

        // =============== FEED ===============
        if (query.isNullOrBlank()) {
            // TVMaze pages are 0-based, your VM uses 1-based -> convert
            val apiPage = (page - 1).coerceAtLeast(0)

            val remoteShows = api.getShows(apiPage)
            val entities = remoteShows.map { show ->
                EventMapper.showToEntity(show, sourceQuery = null)
            }

            if (reset && page == 1) {
                dao.clearRemoteFeedCache()
            }

            dao.upsertAll(entities)
            return entities.size
        }

        // =============== SEARCH ===============
        val remoteSearch = api.searchShows(query)

        // convert -> entities
        val allEntities = remoteSearch.map { dto ->
            EventMapper.searchToEntity(dto, sourceQuery = query)
        }

        // pagination via slicing
        val from = ((page - 1) * pageSize).coerceAtLeast(0)
        val slice = if (from >= allEntities.size) emptyList()
        else allEntities.drop(from).take(pageSize)

        if (reset && page == 1) {
            dao.clearSearchCache(query)
        }

        dao.upsertAll(slice)
        return slice.size
    }
}
