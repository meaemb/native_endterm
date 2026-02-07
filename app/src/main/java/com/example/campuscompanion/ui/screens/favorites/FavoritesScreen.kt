package com.example.campuscompanion.ui.screens.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campuscompanion.viewmodel.EventsViewModel

@Composable
fun FavoritesScreen(
    vm: EventsViewModel,
    onBack: () -> Unit,
    onOpenDetails: (String) -> Unit
) {
    val isLoggedIn by vm.isLoggedIn.collectAsState()
    val events by vm.favoriteEvents.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        TextButton(onClick = onBack) { Text("Back") }
        Text("Favorites", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        if (!isLoggedIn) {
            Text("Please sign in to view favorites", color = MaterialTheme.colorScheme.error)
            return@Column
        }

        if (events.isEmpty()) {
            Text("No favorite events yet ⭐")
            return@Column
        }

        events.forEach { e ->
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onOpenDetails(e.id) }
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(e.title, style = MaterialTheme.typography.titleMedium)
                    Text(e.location, style = MaterialTheme.typography.bodySmall)
                    Text(e.time, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
