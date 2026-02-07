package com.example.campuscompanion

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.campuscompanion.ui.navigation.AppNavGraph
import com.example.campuscompanion.ui.navigation.BottomBar
import com.example.campuscompanion.ui.theme.CampusCompanionTheme
import com.example.campuscompanion.viewmodel.EventsViewModel

@Composable
fun CampusCompanionRoot(vm: EventsViewModel) {
    val navController = rememberNavController()

    CampusCompanionTheme {
        Scaffold(
            bottomBar = { BottomBar(navController) }
        ) { padding ->
            AppNavGraph(
                nav = navController,
                vm = vm,
                modifier = Modifier.padding(padding)
            )
        }
    }
}
