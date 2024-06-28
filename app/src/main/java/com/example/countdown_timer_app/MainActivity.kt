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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.room.Room
import com.example.countdown_timer_app.ui.theme.CountdowntimerappTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class MainActivity : ComponentActivity() {
    private lateinit var eventDao: EventDao

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize the database and get the DAO
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "event-database"
        ).build()
        eventDao = db.eventDao()

        setContent {
            CountdowntimerappTheme {
                // Declare a mutable state for the search query text.
                var searchQuery by remember { mutableStateOf("") }
                // Declare a mutable state to determine if the user is in search mode.
                var isSearching by remember { mutableStateOf(false) }
                // Create a NavController for navigation between composables.
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier
                        .fillMaxSize() // Make the Surface fill the maximum size of the parent.
                        .statusBarsPadding(), // Add padding equivalent to height of the status bar.
                    color = MaterialTheme.colorScheme.background // Set the background color.
                ) {
                    // Define a NavHost to manage navigation between composables.
                    NavHost(navController = navController, startDestination = "home") {
                        // Define the "home" composable destination.
                        composable("home") {
                            // Display home screen layout with search & event adding functionalities
                            HomeScreenLayout(
                                searchQuery = searchQuery,
                                onSearchQueryChange = { searchQuery = it },
                                isSearching = isSearching,
                                onSearchToggle = { isSearching = !isSearching },
                                // Navigate to "new_event" screen when the add event button is clicked.
                                onAddEventClicked = { navController.navigate("new_event") },
                                eventDao = eventDao
                            )
                        }
                        // Define the "new_event" composable destination.
                        composable("new_event") {
                            // Display new event screen layout with back and start functionalities.
                            NewEventScreenLayout(
                                // Navigate back to the previous screen ("home")
                                onBack = { navController.popBackStack() },
                                // Navigate back to home screen
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
            .fillMaxWidth() // Make the Row fill the maximum available width.
            .background(MaterialTheme.colorScheme.primary) // Set background color to primary color of the theme.
            .padding(horizontal = 16.dp, vertical = 8.dp), // Apply horizontal and vertical padding to the Row.
        verticalAlignment = Alignment.CenterVertically, // Align children vertically to the center of the row.
        horizontalArrangement = Arrangement.SpaceBetween // Space children evenly within the Row.
    ) {
        if (isSearching) { // Check if the user is in search mode.
            TextField(
                value = searchQuery, // Set the current value of the TextField to the search query.
                onValueChange = onSearchQueryChange, // Update search query when the text changes.
                placeholder = { Text("Search...") }, // Placeholder text when TextField is empty.
                modifier = Modifier.weight(1f), // Make TextField take up remaining space in Row.
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary // Set the text color to be readable on the primary background.
                )
            )
        } else {
            Text(
                text = stringResource(R.string.App_name), // Display the app name text.
                style = MaterialTheme.typography.titleLarge, // Apply typography style to the text.
                color = MaterialTheme.colorScheme.onPrimary, // Set the text color for readability on the primary background.
                modifier = Modifier.weight(1f), // Make the text take up the remaining space in the Row.
                textAlign = TextAlign.Center // Center the text horizontally within its container.
            )
        }

        Spacer(modifier = Modifier.width(8.dp)) // Adds space between the text and the icon.

        IconButton(
            onClick = onSearchToggle, // Set action to perform when the search button is clicked.
            modifier = Modifier.size(24.dp) // Apply a size modifier to set the height and width of the IconButton.
        ) {
            Icon(
                imageVector = if (isSearching) Icons.Filled.Close else Icons.Filled.Search, // Change icon based on search mode.
                contentDescription = if (isSearching) "Close" else "Search", // Set content description for accessibility.
                tint = MaterialTheme.colorScheme.onPrimary // Set the icon color for contrast against the primary background.
            )
        }
    }
}

@Composable
fun NewEventButton(onAddEventClicked: () -> Unit) {
    // Defines clickable button to add new event
    Button(
        onClick = { onAddEventClicked() }, // Lambda that's executed when the Button is clicked
        modifier = Modifier
            .size(110.dp)
            .padding(start = 8.dp, top = 8.dp), // Position button below the AppBar correctly
        // Sets the shape of the Button corners to be rounded with a 20 dp radius
        shape = RoundedCornerShape(20.dp),
        // Adds a border around the Button with a width of 1 dp and black color
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Text(
            // Sets the text to display on the Button
            text = stringResource(R.string.New_event_String),
            fontSize = 18.sp, // Sets the font size of the text to 18 scalable pixels (sp)
            color = Color.Black // Sets the color of the text to black
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenLayout(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isSearching: Boolean,
    onSearchToggle: () -> Unit,
    onAddEventClicked: () -> Unit,
    eventDao: EventDao
) {
    var events by remember { mutableStateOf(listOf<Event>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            events = eventDao.getAllEvents().sortedByDescending {
                LocalDateTime.of(
                    LocalDate.parse(it.eventDate),
                    LocalTime.parse(it.eventTime)
                )
            }
        }
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
                    items(events) { event ->
                        EventCard(event)
                    }
                }
            }
        }
    }
}

@Composable
fun EventCard(event: Event) {
    Surface(
        modifier = Modifier.size(150.dp),
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
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    CountdowntimerappTheme {
        HomeScreenLayout(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            isSearching = isSearching,
            onSearchToggle = { isSearching = !isSearching },
            onAddEventClicked = { /* TODO: Implement add event functionality */ },
            eventDao = MockEventDao()
        )
    }
}
