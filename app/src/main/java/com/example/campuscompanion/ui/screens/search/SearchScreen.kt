package com.example.campuscompanion.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campuscompanion.viewmodel.EventsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    vm: EventsViewModel,
    onBack: () -> Unit,
    onOpenDetails: (String) -> Unit
) {
    val isLoggedIn by vm.isLoggedIn.collectAsState()
    val query by vm.query.collectAsState()
    val results by vm.searchEvents.collectAsState()
    val loading by vm.loading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }
            )
        }
    ) { padding ->

        if (!isLoggedIn) {
            Column(Modifier.padding(padding).padding(16.dp)) {
                Text("Please sign in or register to use search.")
            }
            return@Scaffold
        }

        Column(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = { vm.setQuery(it) }, // debounce is inside ViewModel
                label = { Text("Type to search") },
                modifier = Modifier.fillMaxWidth()
            )

            if (query.isNotBlank() && results.isEmpty() && !loading) {
                Text("No results")
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
                items(results) { e ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth().clickable { onOpenDetails(e.id) }
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(e.title, style = MaterialTheme.typography.titleMedium)
                            Text(e.location, style = MaterialTheme.typography.bodySmall)
                            Text(e.time, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                item {
                    if (query.isNotBlank()) {
                        Button(
                            onClick = { vm.loadMoreSearch() },
                            enabled = !loading,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (loading) "Loading..." else "Load more")
                        }
                    }
                }
            }
        }
    }
}
