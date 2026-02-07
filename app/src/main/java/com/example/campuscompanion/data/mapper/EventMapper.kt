package com.example.campuscompanion.data.mapper

import com.example.campuscompanion.data.local.entity.EventEntity
import com.example.campuscompanion.data.remote.dto.TvMazeSearchDto
import com.example.campuscompanion.data.remote.dto.TvMazeShowDto
import com.example.campuscompanion.domain.model.Event

object EventMapper {

    // Remote (FEED show) -> Room
    fun showToEntity(show: TvMazeShowDto, sourceQuery: String?): EventEntity =
        EventEntity(
            id = show.id.toString(),
            title = show.name,
            description = show.summary ?: "No description",
            location = "TV Show",
            time = "Anytime",
            imageUrl = show.image?.original,
            isLocal = false,
            updatedAt = System.currentTimeMillis(),
            sourceQuery = sourceQuery
        )

    // Remote (SEARCH dto) -> Room
    fun searchToEntity(dto: TvMazeSearchDto, sourceQuery: String?): EventEntity =
        showToEntity(dto.show, sourceQuery)

    // Room -> Domain
    fun entityToDomain(e: EventEntity): Event =
        Event(
            id = e.id,
            title = e.title,
            description = e.description,
            location = e.location,
            time = e.time,
            imageUrl = e.imageUrl,
            isLocal = e.isLocal,
            updatedAt = e.updatedAt,
            sourceQuery = e.sourceQuery
        )

    // Domain -> Room
    fun domainToEntity(e: Event): EventEntity =
        EventEntity(
            id = e.id,
            title = e.title,
            description = e.description,
            location = e.location,
            time = e.time,
            imageUrl = e.imageUrl,
            isLocal = e.isLocal,
            updatedAt = e.updatedAt,
            sourceQuery = e.sourceQuery
        )
}
