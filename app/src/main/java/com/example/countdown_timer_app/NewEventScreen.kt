package com.example.countdown_timer_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.ContentAlpha
import com.example.countdown_timer_app.ui.theme.CountdowntimerappTheme

class NewEventScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Makes the app content extend into window insets areas like status and navigation bars.
        enableEdgeToEdge()
        setContent {
            CountdowntimerappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize() // Fills the maximum size of the parent.
                        // Adds padding equivalent to the height of the status bar.,
                        .statusBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NewEventScreenLayout()
                }
            }
        }
    }
}

@Composable
fun NewEventScreenAppBar(onBack: () -> Unit, onStart: () -> Unit, isStartEnabled: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Back Icon
        IconButton(
            onClick = onBack,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        // Start Icon
        IconButton(
            onClick = onStart,
            modifier = Modifier.size(24.dp),
            enabled = isStartEnabled
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Start",
                tint = if (isStartEnabled) MaterialTheme.colorScheme.onPrimary
                       else MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled)
            )
        }
    }
}

@Composable
fun NewEventScreenLayout() {
    NewEventScreenAppBar(
        onBack = { /* TODO: Implement back functionality */ },
        onStart = { /* TODO: Implement start functionality */ },
        isStartEnabled = true
    )
}

@Preview(showBackground = true)
@Composable
fun NewEventScreenPreview() {
    CountdowntimerappTheme {
        NewEventScreenLayout()
    }
}
