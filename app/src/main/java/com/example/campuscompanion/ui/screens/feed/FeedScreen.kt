package com.example.campuscompanion.ui.screens.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campuscompanion.domain.model.Event
import com.example.campuscompanion.viewmodel.EventsViewModel
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    vm: EventsViewModel,
    onOpenDetails: (String) -> Unit,
    onOpenSearch: () -> Unit
) {
    val isLoggedIn by vm.isLoggedIn.collectAsState()
    val events by vm.feedEvents.collectAsState()
    val favorites by vm.favorites.collectAsState()
    val msg by vm.uiMessage.collectAsState()
    val loading by vm.loading.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(msg) {
        msg?.let {
            snackbarHostState.showSnackbar(it)
            vm.clearUiMessage()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("TV Shows") },
                actions = {
                    IconButton(onClick = onOpenSearch) {
                        Text("🔍")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        if (!isLoggedIn) {
            Column(
                Modifier.padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Please sign in or register to use the app.")
            }
            return@Scaffold
        }

        if (events.isEmpty() && !loading) {
            Column(
                Modifier.padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("No events yet")
                Button(onClick = { vm.refreshFeed() }) { Text("Refresh") }
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(events) { event ->
                EventCard(
                    event = event,
                    isFav = favorites.contains(event.id),
                    onClick = { onOpenDetails(event.id) },
                    onToggleFav = { vm.toggleFavorite(event.id) }
                )
            }

            item {
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { vm.loadMoreFeed() },
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (loading) "Loading..." else "Load more")
                }
            }
        }
    }
}

@Composable
private fun EventCard(
    event: Event,
    isFav: Boolean,
    onClick: () -> Unit,
    onToggleFav: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            //  IMAGE
            if (!event.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = event.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(onClick = onToggleFav) {
                    Text(if (isFav) "★" else "☆")
                }
            }

            Text(
                text = event.description,
                maxLines = 2,
                style = MaterialTheme.typography.bodyMedium
            )

            Text("📺 Category: ${event.location}", style = MaterialTheme.typography.bodySmall)
            Text("⏰ Air time: ${event.time}", style = MaterialTheme.typography.bodySmall)

        }
    }
}