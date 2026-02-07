package com.example.campuscompanion.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.campuscompanion.ui.navigation.AppNavGraph
import com.example.campuscompanion.ui.theme.CampusCompanionTheme
import com.example.campuscompanion.viewmodel.EventsViewModel

@Composable
fun CampusRoot() {
    val navController = rememberNavController()
    val vm: EventsViewModel = hiltViewModel()

    CampusCompanionTheme {
        AppNavGraph(nav = navController, vm = vm)
    }
}
