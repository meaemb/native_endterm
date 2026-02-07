package com.example.campuscompanion.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.campuscompanion.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    /**
     * FEED:
     * - all events not related to search
     * - includes BOTH remote and locally created events
     */
    @Query("""
        SELECT * FROM events
        WHERE sourceQuery IS NULL
        ORDER BY updatedAt DESC
    """)
    fun observeFeed(): Flow<List<EventEntity>>

    /**
     * SEARCH:
     * - cached results for a specific query
     */
    @Query("""
        SELECT * FROM events
        WHERE sourceQuery = :query
        ORDER BY updatedAt DESC
    """)
    fun observeSearch(query: String): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): EventEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(list: List<EventEntity>)

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun delete(id: String)

    /**
     * Clears ONLY search cache for specific query
     */
    @Query("DELETE FROM events WHERE sourceQuery = :query")
    suspend fun clearSearchCache(query: String)

    /**
     * Clears ONLY remote feed items
     * Keeps locally created events (isLocal = true)
     */
    @Query("""
        DELETE FROM events
        WHERE sourceQuery IS NULL
        AND isLocal = 0
    """)
    suspend fun clearRemoteFeedCache()

    /**
     * Unified refresh logic:
     * - feed → clear only remote items
     * - search → clear only that search cache
     */
    @Transaction
    suspend fun resetAndInsert(
        query: String?,
        items: List<EventEntity>
    ) {
        if (query.isNullOrBlank()) {
            clearRemoteFeedCache()
        } else {
            clearSearchCache(query)
        }
        upsertAll(items)
    }
}
