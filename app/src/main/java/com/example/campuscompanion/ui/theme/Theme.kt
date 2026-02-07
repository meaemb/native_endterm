package com.example.campuscompanion.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val Colors = lightColorScheme(
    primary = Brand
)

@Composable
fun CampusCompanionTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Colors,
        typography = Typography,
        content = content
    )
}
