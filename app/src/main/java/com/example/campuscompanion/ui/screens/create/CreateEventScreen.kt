package com.example.campuscompanion.ui.screens.create

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campuscompanion.viewmodel.EventsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditEventScreen(
    vm: EventsViewModel,
    onBack: () -> Unit,
    editingId: String? = null
) {
    val isLoggedIn by vm.isLoggedIn.collectAsState()
    val loading by vm.loading.collectAsState()
    val selected by vm.selectedEvent.collectAsState()

    LaunchedEffect(editingId) {
        if (editingId != null) vm.selectEvent(editingId)
    }

    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var loc by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    LaunchedEffect(selected?.id, editingId) {
        if (editingId != null && selected?.id == editingId) {
            title = selected?.title ?: ""
            desc = selected?.description ?: ""
            loc = selected?.location ?: ""
            time = selected?.time ?: ""
            imageUrl = selected?.imageUrl ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (editingId == null) "Create Event" else "Edit Event") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }
            )
        }
    ) { padding ->

        if (!isLoggedIn) {
            Column(Modifier.padding(padding).padding(16.dp)) {
                Text("Please sign in or register to create/edit events.")
            }
            return@Scaffold
        }

        Column(
            Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(title, { title = it }, label = { Text("Title*") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(desc, { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(loc, { loc = it }, label = { Text("Location") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(time, { time = it }, label = { Text("Time") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(imageUrl, { imageUrl = it }, label = { Text("Image URL") }, modifier = Modifier.fillMaxWidth())

            Button(
                enabled = !loading,
                onClick = {
                    if (editingId == null) {
                        vm.createEvent(title, desc, loc, time, imageUrl.ifBlank { null })
                    } else {
                        vm.updateEvent(editingId, title, desc, loc, time, imageUrl.ifBlank { null })
                    }
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (loading) "Saving..." else "Save")
            }
        }
    }
}
