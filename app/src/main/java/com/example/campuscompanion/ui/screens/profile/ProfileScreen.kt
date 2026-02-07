package com.example.campuscompanion.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campuscompanion.viewmodel.EventsViewModel

@Composable
fun ProfileScreen(vm: EventsViewModel, onBack: () -> Unit) {
    val isLoggedIn by vm.isLoggedIn.collectAsState()
    val email by vm.email.collectAsState()
    val msg by vm.uiMessage.collectAsState()
    val loading by vm.loading.collectAsState()

    var inputEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        TextButton(onClick = onBack) { Text("Back") }

        Card(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Column(Modifier.padding(12.dp)) {
                Text("Status", style = MaterialTheme.typography.titleMedium)
                Text(if (isLoggedIn) "Signed in as $email" else "Not signed in")
            }
        }

        OutlinedTextField(inputEmail, { inputEmail = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(password, { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(12.dp))

        Button(
            enabled = !loading,
            onClick = { vm.signIn(inputEmail, password) },
            modifier = Modifier.fillMaxWidth()
        ) { Text(if (loading) "Please wait..." else "Sign In") }

        Button(
            enabled = !loading,
            onClick = { vm.signUp(inputEmail, password) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Sign Up") }

        if (isLoggedIn) {
            OutlinedButton(
                enabled = !loading,
                onClick = { vm.signOut() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Sign Out") }
        }

        if (msg != null) {
            Spacer(Modifier.height(12.dp))
            Text(text = msg!!, color = MaterialTheme.colorScheme.primary)
        }
    }
}
