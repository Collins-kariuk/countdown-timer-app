package com.example.countdown_timer_app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.runBlocking
import com.example.countdown_timer_app.ui.theme.CountdowntimerappTheme

class ExistingEventScreen : ComponentActivity() {
    private lateinit var viewModel: EventViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize the database and get the DAO
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "event-database"
        ).build()
        val eventDao = db.eventDao()
        viewModel = ViewModelProvider(
            this,
            EventViewModelFactory(eventDao)
        )[EventViewModel::class.java]

        // Assuming you have the eventId from the intent or some other source
        val eventId = intent.getIntExtra("EVENT_ID", -1)

        setContent {
            CountdowntimerappTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EventScreenLayout(
                        viewModel = viewModel,
                        eventId = eventId,
                        onShareClicked = { /* Implement share functionality */ },
                        onEditClicked = { /* Implement edit functionality */ },
                        onStartClicked = { finish() } // Navigate back to previous screen
                    )
                }
            }
        }
    }
}

/**
 * Composable function that sets up the app bar for the Event Screen.
 * It includes back, edit, and share buttons, and handles their click events.
 *
 * @param onShareClicked Function called when the share icon is clicked.
 * @param onEditClicked Function called when the edit icon is clicked.
 * @param onStartClicked Function called when the back icon is clicked.
 */
@Composable
fun EventScreenAppBar(
    onShareClicked: () -> Unit,
    onEditClicked: () -> Unit,
    onStartClicked: () -> Unit
) {
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

/**
 * Composable function that displays the details of an event, including a countdown timer.
 *
 * @param eventTitle The title of the event.
 * @param eventDate The date and time of the event as a string.
 * @param eventNote An optional note associated with the event.
 */
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

/**
 * Composable function that sets up the layout for the Event Screen.
 * It fetches the event details based on the event ID and displays the event details
 * along with the app bar containing the share, edit, and back options.
 *
 * @param viewModel The ViewModel managing the event data.
 * @param eventId The unique ID of the event to be displayed.
 * @param onShareClicked Function called when the share icon is clicked.
 * @param onEditClicked Function called when the edit icon is clicked.
 * @param onStartClicked Function called when the back icon is clicked.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventScreenLayout(
    viewModel: EventViewModel,
    eventId: Int, // Assuming events have unique IDs
    onShareClicked: () -> Unit,
    onEditClicked: () -> Unit,
    onStartClicked: () -> Unit
) {
    val events by viewModel.events.collectAsState()
    val event = events.firstOrNull { it.id == eventId }

    Scaffold(
        topBar = {
            EventScreenAppBar(
                onShareClicked = onShareClicked,
                onEditClicked = onEditClicked,
                onStartClicked = onStartClicked
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.tertiaryContainer),
            color = MaterialTheme.colorScheme.background
        ) {
            event?.let {
                EventDetailScreen(
                    eventTitle = it.eventName,
                    eventDate = "${it.eventDate} ${it.eventTime}",
                    eventNote = it.eventNotes
                )
            } ?: Text(
                text = "Event not found",
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun EventScreenPreview() {
    val mockEventDao = MockEventDao().apply {
        val sampleEvent = Event(
            id = 1,
            eventName = "Sample Event",
            eventDate = "2024-05-15",
            eventTime = "14:00",
            eventNotes = "Sample event notes.",
            eventLocation = "Sample Location"
        )
        runBlocking { insertEvent(sampleEvent) }
    }
    val mockViewModel = EventViewModel(mockEventDao)

    CountdowntimerappTheme {
        EventScreenLayout(
            viewModel = mockViewModel,
            eventId = 1,
            onShareClicked = { /* No-op for preview */ },
            onEditClicked = { /* No-op for preview */ },
            onStartClicked = { /* No-op for preview */ }
        )
    }
}
