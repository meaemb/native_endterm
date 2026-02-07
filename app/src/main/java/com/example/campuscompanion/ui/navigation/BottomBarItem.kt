package com.example.campuscompanion.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Feed : BottomBarItem(Routes.FEED, Icons.Default.Home, "Feed")
    object Favorites : BottomBarItem(Routes.FAVORITES, Icons.Default.Star, "Favorites")
    object Create : BottomBarItem(Routes.CREATE, Icons.Default.Add, "Create")
    object Profile : BottomBarItem(Routes.PROFILE, Icons.Default.Person, "Profile")
}
