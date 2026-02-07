package com.example.campuscompanion

import com.example.campuscompanion.viewmodel.EventsViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class EventsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun createEvent_requiresLogin() = runTest {
        val vm = EventsViewModel(FakeEventsRepo(), FakeUserRepo())

        vm.createEvent("Title", "D", "L", "T", null)

        assertEquals(
            "Please sign in or register to use this feature.",
            vm.uiMessage.value
        )
    }

    @Test
    fun createEvent_requiresTitle() = runTest {
        val user = FakeUserRepo().apply {
            uid = "u1"
            email = "a@b.com"
        }

        val vm = EventsViewModel(FakeEventsRepo(), user)

        vm.createEvent("   ", "D", "L", "T", null)

        assertEquals("Title is required", vm.uiMessage.value)
    }

    @Test
    fun refreshFeed_networkFailure_setsMessage() = runTest {
        val repo = FakeEventsRepo().apply { shouldFailRefresh = true }
        val user = FakeUserRepo().apply { uid = "u1" }

        val vm = EventsViewModel(repo, user)

        vm.refreshFeed()

        assertNotNull(vm.uiMessage.value)
    }
}
