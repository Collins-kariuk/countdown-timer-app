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

class EventScreen : ComponentActivity() {
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
        // Start Icon
        IconButton(
            onClick = onStartClicked,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow, // Assuming a play arrow for "Start"
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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