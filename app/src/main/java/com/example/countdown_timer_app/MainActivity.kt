package com.example.countdown_timer_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.countdown_timer_app.ui.theme.CountdowntimerappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Makes the app content extend into window insets areas like status and navigation bars
        enableEdgeToEdge()
        setContent {
            CountdowntimerappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize() // Fills the maximum size of the parent
                        .statusBarsPadding(), // Adds padding equal to the height of the status bar
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreenLayout( onAddEventClicked = { } )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenAppBar(
    searchQuery: String, // Current search query text.
    onSearchQueryChange: (String) -> Unit, // Callback to update the search query text.
    isSearching: Boolean, // Flag to determine if the user is in search mode.
    onSearchToggle: () -> Unit // Callback to toggle the search mode.
) {
    Row(
        modifier = Modifier
            .fillMaxWidth() // Make the Row fill the maximum available width.
            // Set background color to primary color of the theme.
            .background(MaterialTheme.colorScheme.primary)
            // Apply horizontal and vertical padding to the Row.
            .padding(horizontal = 16.dp, vertical = 8.dp),
        // Align children vertically to the center of the row.
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Space children evenly within the Row.
    ) {
        if (isSearching) { // Check if the user is in search mode.
            TextField(
                value = searchQuery, // Set the current value of the TextField to the search query.
                onValueChange = onSearchQueryChange, // Update search query when the text changes.
                // Placeholder text when TextField is empty.
                placeholder = { Text("Search...") },
                modifier = Modifier.weight(1f), // Make TextField take up remaining space in Row.
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    // Set the text color to be readable on the primary background.
                    color = MaterialTheme.colorScheme.onPrimary
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        } else {
            Text(
                text = stringResource(R.string.App_name), // Display the app name text.
                style = MaterialTheme.typography.titleLarge, // Apply typography style to the text.
                // Set the text color for readability on the primary background.
                color = MaterialTheme.colorScheme.onPrimary,
                // Make the text take up the remaining space in the Row.
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center // Center the text horizontally within its container.
            )
        }

        Spacer(modifier = Modifier.width(8.dp)) // Adds space between the text and the icon.

        IconButton(
            onClick = onSearchToggle, // Set action to perform when the search button is clicked.
            // Apply a size modifier to set the height and width of the IconButton.
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                // Change icon based on search mode.
                imageVector = if (isSearching) Icons.Filled.Close else Icons.Filled.Search,
                // Set content description for accessibility.
                contentDescription = if (isSearching) "Close" else "Search",
                // Set the icon color for contrast against the primary background.
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun NewEventScreen(onAddEventClicked: () -> Unit) {
    // Defines clickable button to add new event
    Button(
        onClick = { onAddEventClicked() }, // Lambda that's executed when the Button is clicked
        modifier = Modifier
            .size(110.dp) // Makes the button square-shaped by setting equal width and height
            // Adds padding to position it below the AppBar correctly
            .padding(start = 8.dp, top = 8.dp),
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

@Composable
fun HomeScreenLayout(onAddEventClicked: () -> Unit) {
    // Scaffold composable provides a layout structure with a top bar and a content area
    Scaffold(
        topBar = {
            HomeScreenAppBar(
                searchQuery = "",
                onSearchQueryChange = {},
                isSearching = false,
                onSearchToggle = {}
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                // Aligns content to the start horizontally and top vertically
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                // New Event button positioned right below the AppBar at the top left
                NewEventScreen(onAddEventClicked)
                // Adds space between button & other content
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
fun EventList(events: List<Event>) {
    LazyColumn {
        items(events) { event ->
            Text(event.name)
        }
    }
}

@Composable
fun HomeScreen(events: List<Event>) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    val filteredEvents = if (isSearching) {
        events.filter { it.name.contains(searchQuery, ignoreCase = true) }
    } else {
        events
    }

    Column {
        HomeScreenAppBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            isSearching = isSearching,
            onSearchToggle = { isSearching = !isSearching }
        )

        EventList(events = filteredEvents)
    }
}

data class Event(val name: String)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val sampleEvents = listOf(
        Event("Birthday Party"),
        Event("Conference"),
        Event("Wedding"),
        Event("Meeting")
    )

    MaterialTheme {
        HomeScreen(events = sampleEvents)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    CountdowntimerappTheme {
//        HomeScreenLayout(onAddEventClicked = { })
//    }
//}