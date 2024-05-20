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
        val dateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault())
        val eventDateObject = dateFormat.parse(eventDate)

        // Lambda function to update the time remaining
        val updateRemainingTime: () -> Unit = {
            eventDateObject?.let {
                val currentTime = System.currentTimeMillis()
                val eventTime = it.time
                val difference = eventTime - currentTime

                if (difference > 0) {
                    val days = TimeUnit.MILLISECONDS.toDays(difference)
                    val hours = TimeUnit.MILLISECONDS.toHours(difference) % 24
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(difference) % 60
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(difference) % 60

                    // Added Locale.getDefault() to the String.format method to specify the locale
                    // explicitly
                    timeRemaining = String.format(Locale.getDefault(),
                        "%02d Days %02d Hours %02d Minutes %02d Seconds",
                        days, hours, minutes, seconds)
                } else {
                    timeRemaining = "Event has passed"
                }
            }
        }

        // Refactored the loop to check the condition in the while loop itself (while (timeRemaining
        // != "Event has passed")), thus removing the need for break.
        while (timeRemaining != "Event has passed") {
            updateRemainingTime()
            delay(1000L) // Update every second
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display the event title
        Text(
            text = eventTitle,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        // Display the event date
        Text(
            text = eventDate,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        // Display the countdown timer
        Text(
            text = timeRemaining,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // Display the event note, if available
        eventNote?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun EventScreenLayout(onShareClicked: () -> Unit,
                      onEditClicked: () -> Unit,
                      onStartClicked: () -> Unit) {
    Column {
        EventScreenAppBar(onShareClicked = onShareClicked,
            onEditClicked = onEditClicked,
            onStartClicked = onStartClicked)

        EventDetailScreen(
            eventTitle = "Meeting with Team",
            eventDate = "2024-05-15 14:00",
            eventNote = "Discuss quarterly goals."
        )
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