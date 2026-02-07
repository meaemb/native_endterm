package com.example.campuscompanion

import com.example.campuscompanion.data.mapper.EventMapper
import com.example.campuscompanion.data.remote.dto.TvMazeImageDto
import com.example.campuscompanion.data.remote.dto.TvMazeShowDto
import org.junit.Assert.assertEquals
import org.junit.Test

class EventMapperTest {

    @Test
    fun showToEntity_mapsCorrectly() {
        val show = TvMazeShowDto(
            id = 1,
            name = "Test Show",
            summary = "Summary",
            image = TvMazeImageDto("http://img")
        )

        val entity = EventMapper.showToEntity(show, sourceQuery = null)

        assertEquals("1", entity.id)
        assertEquals("Test Show", entity.title)
        assertEquals("TV Show", entity.location)
    }

    @Test
    fun entityToDomain_mapsCorrectly() {
        val entity = EventMapper.showToEntity(
            TvMazeShowDto(1, "A", null, null),
            sourceQuery = "abc"
        )

        val domain = EventMapper.entityToDomain(entity)

        assertEquals("A", domain.title)
        assertEquals("abc", domain.sourceQuery)
    }
}
