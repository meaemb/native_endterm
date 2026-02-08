package com.example.campuscompanion

import com.example.campuscompanion.data.mapper.EventMapper
import com.example.campuscompanion.data.remote.dto.TvMazeImageDto
import com.example.campuscompanion.data.remote.dto.TvMazeShowDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class MoreUnitTests {

    @Test
    fun mapper_handlesNullImage() {
        val show = TvMazeShowDto(
            id = 1,
            name = "Show",
            summary = "Summary",
            image = null
        )

        val entity = EventMapper.showToEntity(show, sourceQuery = null)

        assertNull(entity.imageUrl)
        assertEquals("TV Show", entity.location)
        assertEquals("Anytime", entity.time)
    }

    @Test
    fun mapper_handlesNullSummary_setsDefaultDescription() {
        val show = TvMazeShowDto(
            id = 2,
            name = "Show2",
            summary = null,
            image = TvMazeImageDto(original = "http://img")
        )

        val entity = EventMapper.showToEntity(show, sourceQuery = null)

        assertEquals("No description", entity.description)
    }

    @Test
    fun userRepo_signOut_clearsUserData() = runTest {
        val user = FakeUserRepo()
        user.signIn("a@b.com", "123456")
        user.toggleFavorite("1")

        user.signOut()

        assertNull(user.currentUserId())
        assertNull(user.currentUserEmail())
        assertTrue(user.getFavoriteIds().isEmpty())
    }

    @Test
    fun toggleFavorite_doesNotCrashWhenLoggedIn() = runTest {
        val user = FakeUserRepo()
        user.signIn("a@b.com", "123456")

        user.toggleFavorite("10") // просто вызываем

        assertTrue(true) // если дошли сюда — тест прошёл
    }


    @Test(expected = IllegalStateException::class)
    fun userRepo_toggleFavorite_requiresLogin() = runTest {
        val user = FakeUserRepo()
        user.toggleFavorite("1")
    }
}
