package com.example.campuscompanion.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.campuscompanion.ui.screens.create.CreateEditEventScreen
import com.example.campuscompanion.ui.screens.details.DetailsScreen
import com.example.campuscompanion.ui.screens.favorites.FavoritesScreen
import com.example.campuscompanion.ui.screens.feed.FeedScreen
import com.example.campuscompanion.ui.screens.profile.ProfileScreen
import com.example.campuscompanion.ui.screens.search.SearchScreen
import com.example.campuscompanion.viewmodel.EventsViewModel

object Routes {
    const val FEED = "feed"
    const val FAVORITES = "favorites"
    const val SEARCH = "search"
    const val CREATE = "create"
    const val EDIT = "edit/{id}"
    const val PROFILE = "profile"
    const val DETAILS = "details/{id}"

    fun details(id: String) = "details/$id"
    fun edit(id: String) = "edit/$id"
}

@Composable
fun AppNavGraph(
    nav: NavHostController,
    vm: EventsViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(navController = nav, startDestination = Routes.FEED, modifier = modifier) {

        composable(Routes.FEED) {
            FeedScreen(
                vm = vm,
                onOpenDetails = { id -> nav.navigate(Routes.details(id)) },
                onOpenSearch = { nav.navigate(Routes.SEARCH) }
            )
        }

        composable(Routes.SEARCH) {
            SearchScreen(
                vm = vm,
                onBack = { nav.popBackStack() },
                onOpenDetails = { id -> nav.navigate(Routes.details(id)) }
            )
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen(
                vm = vm,
                onBack = { nav.popBackStack() },
                onOpenDetails = { id -> nav.navigate(Routes.details(id)) }
            )
        }

        composable(Routes.CREATE) {
            CreateEditEventScreen(
                vm = vm,
                onBack = { nav.popBackStack() },
                editingId = null
            )
        }

        composable(
            route = Routes.EDIT,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("id") ?: return@composable
            CreateEditEventScreen(vm = vm, onBack = { nav.popBackStack() }, editingId = id)
        }

        composable(Routes.PROFILE) {
            ProfileScreen(vm = vm, onBack = { nav.popBackStack() })
        }

        composable(
            route = Routes.DETAILS,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("id") ?: return@composable
            DetailsScreen(
                vm = vm,
                eventId = id,
                onBack = { nav.popBackStack() },
                onEdit = { editId -> nav.navigate(Routes.edit(editId)) }
            )
        }
    }
}
