package com.example.countdown_timer_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import com.example.countdown_timer_app.ui.theme.CountdowntimerappTheme

class ExistingEventScreen : ComponentActivity() {
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
                    EventScreenLayout(
                        onShareClicked = {},
                        onEditClicked = {},
                        onStartClicked = {}
                    )
                }
            }
        }
    }
}

@Composable
fun EventScreenAppBar(onShareClicked: () -> Unit, onEditClicked: () -> Unit, onStartClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        // Back Icon
        IconButton(
            onClick = onStartClicked,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Start",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        // Edit Icon
        IconButton(
            onClick = onEditClicked,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit, // Standard edit icon
                contentDescription = "Edit",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        // Share Icon
        IconButton(
            onClick = onShareClicked,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "Share",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun EventDetailScreen(eventTitle: String, eventDate: String, eventNote: String?) {
    // State to hold the remaining time as a string
    var timeRemaining by remember { mutableStateOf("") }

    // Function to calculate and update the remaining time
    LaunchedEffect(Unit) {
        // Format for the date and time input
        val dateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault())
        // Parse the event date string into a Date object
        val eventDateObject = dateFormat.parse(eventDate)

        // Lambda function to update the time remaining
        val updateRemainingTime: () -> Unit = {
            eventDateObject?.let {
                val currentTime = System.currentTimeMillis()
                val eventTime = it.time
                val difference = eventTime - currentTime

                if (difference > 0) {
                    // Calculate days, hours, minutes, and seconds remaining
                    val days = TimeUnit.MILLISECONDS.toDays(difference)
                    val hours = TimeUnit.MILLISECONDS.toHours(difference) % 24
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(difference) % 60
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(difference) % 60

                    // Update the time remaining string
                    timeRemaining = String.format(Locale.getDefault(),
                        "%02d Days %02d Hours %02d Minutes %02d Seconds",
                        days, hours, minutes, seconds)
                } else {
                    timeRemaining = "Event has passed"
                }
            }
        }

        // Continuously update the remaining time every second until the event has passed
        while (timeRemaining != "Event has passed") {
            updateRemainingTime()
            delay(1000L) // Update every second
        }
    }

    // Layout for the event details
    Column(
        modifier = Modifier
            .fillMaxWidth() // Fill the width of the parent container
            .padding(16.dp), // Apply padding around the Column
        horizontalAlignment = Alignment.CenterHorizontally // Center children horizontally
    ) {
        // Display the event title
        Text(
            text = eventTitle,
            style = MaterialTheme.typography.displayLarge, // Large display style for the title
            color = MaterialTheme.colorScheme.onSurface // Text color from the theme
        )
        // Display the event date
        Text(
            text = eventDate,
            style = MaterialTheme.typography.headlineMedium, // Medium headline style for the date
            color = MaterialTheme.colorScheme.onSurface // Text color from the theme
        )
        // Display the countdown timer
        Text(
            text = timeRemaining,
            style = MaterialTheme.typography.displayMedium, // Medium display style for the timer
            color = MaterialTheme.colorScheme.primary, // Primary color from the theme
            fontWeight = FontWeight.Bold, // Bold font weight for emphasis
            modifier = Modifier.padding(bottom = 16.dp) // Bottom padding for spacing
        )
        // Display the event note, if available
        eventNote?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge, // Large body style for the note
                color = MaterialTheme.colorScheme.onSurface // Text color from the theme
            )
        }
    }
}

@Composable
fun EventScreenLayout(onShareClicked: () -> Unit,
                      onEditClicked: () -> Unit,
                      onStartClicked: () -> Unit) {
    // Scaffold composable provides a layout structure with a top bar and a content area
    Scaffold(
        topBar = {
            EventScreenAppBar(
                onShareClicked = onShareClicked,
                onEditClicked = onEditClicked,
                onStartClicked = onStartClicked
            )
        }
    ) { innerPadding ->
        // Surface composable provides a background for the app's content area
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.tertiaryContainer),
            color = MaterialTheme.colorScheme.background
        ) {
            EventDetailScreen(
                eventTitle = "Meeting with Team",
                eventDate = "2024-05-15 14:00",
                eventNote = "Discuss quarterly goals."
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventScreenPreview() {
    CountdowntimerappTheme {
        EventScreenLayout(
            onShareClicked = {},
            onEditClicked = {},
            onStartClicked = {}
        )
    }
}