package com.example.countdown_timer_app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.countdown_timer_app.ui.theme.CountdowntimerappTheme
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    private lateinit var eventDao: EventDao
    private lateinit var viewModel: EventViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "event-database"
        ).build()
        eventDao = db.eventDao()
        viewModel = ViewModelProvider(
            this,
            EventViewModelFactory(eventDao)
        )[EventViewModel::class.java]

        setContent {
            CountdowntimerappTheme {
                val navController = rememberNavController()
                var searchQuery by remember { mutableStateOf("") }
                var isSearching by remember { mutableStateOf(false) }

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreenLayout(
                                viewModel = viewModel,
                                searchQuery = searchQuery,
                                onSearchQueryChange = { searchQuery = it },
                                isSearching = isSearching,
                                onSearchToggle = { isSearching = !isSearching },
                                onAddEventClicked = { navController.navigate("new_event") }
                            )
                        }
                        composable("new_event") {
                            NewEventScreenLayout(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onStart = { navController.navigate("home") },
                                eventDao = eventDao
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable function that displays the AppBar for the Home screen.
 *
 * The AppBar contains a search field that toggles between search mode and a title
 * depending on the current state. When in search mode, the search field allows the user
 * to input a search query. There is also an icon button to toggle the search mode.
 *
 * @param searchQuery The current search query text.
 * @param onSearchQueryChange Callback to update the search query text.
 * @param isSearching Flag to determine if the user is in search mode.
 * @param onSearchToggle Callback to toggle the search mode.
 */
@Composable
fun HomeScreenAppBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isSearching: Boolean,
    onSearchToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (isSearching) {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search...") },
                modifier = Modifier.weight(1f),
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        } else {
            Text(
                text = stringResource(R.string.App_name),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onSearchToggle,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = if (isSearching) Icons.Filled.Close else Icons.Filled.Search,
                contentDescription = if (isSearching) "Close" else "Search",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

/**
 * Composable function that displays a button for adding a new event.
 *
 * This function creates a styled button that, when clicked, triggers a lambda function
 * to handle the addition of a new event. The button is styled with rounded corners, a border,
 * and text indicating its purpose.
 *
 * @param onAddEventClicked Lambda function that is executed when the button is clicked.
 */
@Composable
fun NewEventButton(onAddEventClicked: () -> Unit) {
    Button(
        onClick = { onAddEventClicked() },
        modifier = Modifier
            .size(110.dp)
            .padding(start = 8.dp, top = 8.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Text(
            text = stringResource(R.string.New_event_String),
            fontSize = 18.sp,
            color = Color.Black
        )
    }
}

/**
 * Composable function that displays the home screen layout of the application,
 * including the app bar, a search feature, and a grid of events.
 *
 * @param viewModel The ViewModel that manages the data for the events.
 * @param searchQuery The current search query entered by the user.
 * @param onSearchQueryChange A lambda function to handle changes in the search query.
 * @param isSearching A boolean flag indicating whether the user is currently searching.
 * @param onSearchToggle A lambda function to handle toggling the search mode.
 * @param onAddEventClicked A lambda function to handle the "Add Event" button click.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenLayout(
    viewModel: EventViewModel,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isSearching: Boolean,
    onSearchToggle: () -> Unit,
    onAddEventClicked: () -> Unit
) {
    val events by viewModel.events.collectAsState()

    val filteredEvents = if (searchQuery.isNotBlank()) {
        events.filter { it.eventName.contains(searchQuery, ignoreCase = true) }
    } else {
        events
    }

    Scaffold(
        topBar = {
            HomeScreenAppBar(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                isSearching = isSearching,
                onSearchToggle = onSearchToggle
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                NewEventButton(onAddEventClicked)
                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // Adjust the number of columns as needed
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredEvents) { event ->
                        EventCard(event = event, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

/**
 * Composable function that displays an individual event card, including the event's name,
 * date, time, and optional notes. The card also provides a delete button for removing the event.
 *
 * @param event The event data object containing the event's details such as name, date, time, and
 * notes.
 * @param viewModel The ViewModel that manages the event data, allowing for operations such as
 * deletion.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventCard(event: Event, viewModel: EventViewModel) {
    Box(
        modifier = Modifier.size(150.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.Gray),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = event.eventName, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "${event.eventDate} ${event.eventTime}", style = MaterialTheme.typography.bodySmall)
                event.eventNotes?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = it, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        IconButton(
            onClick = {
                viewModel.deleteEvent(event)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Event")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    val mockEventDao = MockEventDao().apply {
        // Add some sample events within a runBlocking block
        runBlocking {
            val sampleEvents = listOf(
                Event(
                    id = 1,
                    eventName = "Meeting",
                    eventDate = "2024-05-15",
                    eventTime = "14:00",
                    eventNotes = "Discuss quarterly goals.",
                    eventLocation = "Office"
                ),
                Event(
                    id = 2,
                    eventName = "Birthday Party",
                    eventDate = "2024-06-22",
                    eventTime = "18:00",
                    eventNotes = "At John's place.",
                    eventLocation = "Home"
                )
            )
            sampleEvents.forEach { insertEvent(it) }
        }
    }

    val mockViewModel = EventViewModel(mockEventDao)

    CountdowntimerappTheme {
        HomeScreenLayout(
            viewModel = mockViewModel,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            isSearching = isSearching,
            onSearchToggle = { isSearching = !isSearching },
            onAddEventClicked = { /* No-op for preview */ }
        )
    }
}