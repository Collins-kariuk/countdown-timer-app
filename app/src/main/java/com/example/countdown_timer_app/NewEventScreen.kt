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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    NewEventScreenLayout(
                        onBack = { /* TODO: Handle back action */ },
                        onStart = { /* TODO: Handle start action */ },
                        isStartEnabled = true // Replace with actual logic to enable/disable
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEventScreenAppBar(onBack: () -> Unit, onStart: () -> Unit, isStartEnabled: Boolean) {
    TopAppBar(
        title = {
            Text(
                text = "Add New Event",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        actions = {
            IconButton(onClick = onStart, enabled = isStartEnabled) {
                Icon(
                    imageVector = Icons.Default.PlayArrow, // Assuming a play arrow for "Start"
                    contentDescription = stringResource(id = R.string.start),
                    tint = if (isStartEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled)
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
//        backgroundColor = MaterialTheme.colorScheme.primary,
//        contentColor = MaterialTheme.colorScheme.onPrimary,
//        elevation = 4.dp
    )
}

@Composable
fun NewEventScreenLayout(onBack: () -> Unit, onStart: () -> Unit, isStartEnabled: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        NewEventScreenAppBar(onBack, onStart, isStartEnabled)

        Spacer(modifier = Modifier.height(16.dp))

        var eventName by remember { mutableStateOf("") }
        var eventDate by remember { mutableStateOf("") }
        var eventTime by remember { mutableStateOf("") }
        var eventNote by remember { mutableStateOf("") }

        OutlinedTextField(
            value = eventName,
            onValueChange = { eventName = it },
            label = { Text("Event Name") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Button(
            onClick = { /* TODO: Show DatePicker and set eventDate */ },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text(text = if (eventDate.isEmpty()) "Select Date" else eventDate)
        }

        Button(
            onClick = { /* TODO: Show TimePicker and set eventTime */ },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text(text = if (eventTime.isEmpty()) "Select Time" else eventTime)
        }

        OutlinedTextField(
            value = eventNote,
            onValueChange = { eventNote = it },
            label = { Text("Event Note (Optional)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onBack) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    if (eventName.isNotEmpty() && eventDate.isNotEmpty() && eventTime.isNotEmpty()) {
                        onStart()
                    }
                },
                enabled = isStartEnabled
            ) {
                Text("Save")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewEventScreenPreview() {
    CountdowntimerappTheme {
        NewEventScreenLayout(
            onBack = { /* TODO: Handle back action */ },
            onStart = { /* TODO: Handle start action */ },
            isStartEnabled = true // Replace with actual logic to enable/disable
        )
    }
}
