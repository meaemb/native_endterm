package com.example.campuscompanion.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.campuscompanion.viewmodel.EventsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    vm: EventsViewModel,
    eventId: String,
    onBack: () -> Unit,
    onEdit: (String) -> Unit
) {
    LaunchedEffect(eventId) {
        vm.selectEvent(eventId)
    }

    val isLoggedIn by vm.isLoggedIn.collectAsState()
    val event by vm.selectedEvent.collectAsState()
    val favorites by vm.favorites.collectAsState()
    val comments by vm.comments.collectAsState()
    val msg by vm.uiMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(msg) {
        msg?.let {
            snackbarHostState.showSnackbar(it)
            vm.clearUiMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TV Show") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        if (!isLoggedIn) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Please sign in or register to use the app.")
            }
            return@Scaffold
        }

        if (event == null) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val e = event!!
        val isFav = favorites.contains(e.id)

        /** 🔥 ВЕСЬ ЭКРАН — ОДИН LazyColumn */
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            /** 🔹 EVENT CARD */
            item {
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        if (!e.imageUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = e.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                            )
                        }

                        Text(e.title, style = MaterialTheme.typography.titleLarge)
                        Text(e.description)
                        Text("📺 Category: ${e.location}", style = MaterialTheme.typography.bodySmall)
                        Text("⏰ Air time: ${e.time}", style = MaterialTheme.typography.bodySmall)



                        Button(
                            onClick = { vm.toggleFavorite(e.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (isFav) "Remove ⭐" else "Add ⭐")
                        }

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { onEdit(e.id) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Edit")
                            }

                            Button(
                                onClick = {
                                    vm.deleteEvent(e.id)
                                    onBack()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }

            /** 🔹 COMMENTS TITLE */
            item {
                Text(
                    text = "Comments",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            /** 🔹 COMMENTS LIST */
            items(comments) { c ->
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text(c.userEmail, style = MaterialTheme.typography.labelMedium)
                        Text(c.text)
                    }
                }
            }

            /** 🔹 ADD COMMENT */
            item {
                var commentText by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    label = { Text("Write a comment") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        vm.addComment(commentText)
                        commentText = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Send")
                }
            }
        }
    }
}
