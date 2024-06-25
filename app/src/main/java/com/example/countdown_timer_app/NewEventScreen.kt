package com.example.countdown_timer_app

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.room.Room
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.example.countdown_timer_app.ui.theme.CountdowntimerappTheme

class NewEventScreen : ComponentActivity() {
    // Declare a variable for accessing the EventDao
    private lateinit var eventDao: EventDao

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Makes the app content extend into window insets areas like status and navigation bars
        enableEdgeToEdge()

        // Initialize the database and get the DAO
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, // Database class
            "event-database" // Name of the database
        ).build()

        // Assign the DAO to the eventDao variable
        eventDao = db.eventDao()

        setContent {
            // Apply the app theme
            CountdowntimerappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize() // Fills the maximum size of the parent
                        // Adds padding equivalent to the height of the status bar
                        .statusBarsPadding(),
                    color = MaterialTheme.colorScheme.background // Set the background color
                ) {
                    NewEventScreenLayout(
                        onBack = { finish() }, // Navigate back to the previous screen
                        onStart = { finish() }, // Navigate back to home
                        isStartEnabled = true, // Enable the start button
                        eventDao = eventDao // Pass the event DAO to the layout
                    )
                }
            }
        }
    }
}

// A composable function that defines an AppBar with a back button and a start button.
@Composable
fun NewEventScreenAppBar(onBack: () -> Unit) {
    Row(
        // A Row composable to arrange its children horizontally.
        modifier = Modifier
            .fillMaxWidth() // Makes the Row fill the maximum width of its parent.
            // Sets the background color to the primary color from the theme.
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp, vertical = 8.dp), // Adds horizontal and vertical padding.
        verticalAlignment = Alignment.CenterVertically, // Aligns children vertically in the center.
        // Arranges children with space between them.
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Back Icon Button
        IconButton(
            onClick = onBack, // Sets the onClick action to the onBack function.
            modifier = Modifier.size(24.dp) // Sets the size of the IconButton to 24 dp.
        ) {
            Icon(
                // Sets the icon to an auto-mirrored back arrow.
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back", // Provides a content description for accessibility.
                // Sets the icon color to onPrimary from the theme.
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun EditTextField(
    label: String, // The label for the text field
    value: String, // The current text value of the text field
    onValueChanged: (String) -> Unit, // A lambda function to handle changes to the text value
    modifier: Modifier = Modifier, // Modifier for styling the text field
    // Keyboard options to specify keyboard behavior and input type
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true, // Whether the text field is single line or multi-line
    // Visual transformation to apply to the text (e.g., password masking)
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        value = value, // The current text value of the text field
        onValueChange = onValueChanged, // A lambda function to handle changes to the text value
        label = { Text(label) }, // The label to display when the text field is empty
        singleLine = singleLine, // Whether the text field is single line or multi-line
        keyboardOptions = keyboardOptions, // To specify keyboard behavior and input type
        visualTransformation = visualTransformation, // Visual transformation to apply to the text
        // Modifier to style the text field, making it fill the maximum width of its parent
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun DateAndTimeInput(
    onDateChanged: (String) -> Unit, // Lambda function to handle changes to the date
    onTimeChanged: (String) -> Unit // Lambda function to handle changes to the time
) {
    val context = LocalContext.current // Get the current context
    // State variables for the text displayed on the date and time buttons
    var dateButtonText by remember { mutableStateOf("Set Date") }
    var timeButtonText by remember { mutableStateOf("Set Time") }

    // Create a date picker dialog
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // Format the selected date and update the button text and state
            val date = "${String.format("%02d", month + 1)}/${String.format("%02d", dayOfMonth)}/$year"
            onDateChanged(date)
            dateButtonText = date
        },
        2024, 6, 22 // Set the default date to June 22, 2024
    )

    // Create a time picker dialog
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            // Format the selected time and update the button text and state
            val time = String.format("%02d:%02d", hourOfDay, minute)
            onTimeChanged(time)
            timeButtonText = time
        },
        // Set the default time to 12:00 PM and use 24-hour format
        12, 0, false
    )

    // Create a column to hold the date and time buttons
    Column(modifier = Modifier.fillMaxWidth()) {
        // Create a row to arrange the date and time buttons horizontally
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date picker button
            Button(
                onClick = {
                    datePickerDialog.show() // Show the date picker dialog when button is clicked
                },
                modifier = Modifier.weight(1f) // Make the button take up equal space
            ) {
                Text(dateButtonText) // Display the current date text on the button
            }

            Spacer(modifier = Modifier.width(16.dp)) // Add space between the date and time buttons

            // Time picker section
            Row(modifier = Modifier.weight(1f)) {
                Button(
                    onClick = {
                        timePickerDialog.show() // Show time picker dialog when button is clicked
                    },
                    modifier = Modifier.weight(1f) // Make the button take up equal space
                ) {
                    Text(timeButtonText) // Display the current time text on the button
                }
            }
        }
    }
}

@Composable
fun EventDetailsInput(
    eventName: String,
    onEventNameChange: (String) -> Unit,
    eventLocation: String,
    onEventLocationChange: (String) -> Unit,
    eventNotes: String,
    onEventNotesChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.enter_event_details),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )

        // Event Name
        EditTextField(
            label = stringResource(R.string.event_name),
            value = eventName,
            onValueChanged = onEventNameChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Event Location
        EditTextField(
            label = stringResource(R.string.event_location),
            value = eventLocation,
            onValueChanged = onEventLocationChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Event Notes (Optional)
        EditTextField(
            label = stringResource(R.string.optional_event_note),
            value = eventNotes,
            onValueChanged = onEventNotesChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next),
            singleLine = false,
            modifier = Modifier.height(100.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewEventScreenLayout(
    onBack: () -> Unit,
    onStart: () -> Unit,
    isStartEnabled: Boolean,
    eventDao: EventDao
) {
    val scope = rememberCoroutineScope()
    var eventName by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var eventTime by remember { mutableStateOf("") }
    var eventNote by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        NewEventScreenAppBar(onBack)

        Spacer(modifier = Modifier.height(16.dp))

        // Other input fields for event details
        EventDetailsInput(
            eventName = eventName,
            onEventNameChange = { eventName = it },
            eventLocation = eventLocation,
            onEventLocationChange = { eventLocation = it },
            eventNotes = eventNote,
            onEventNotesChange = { eventNote = it }
        )
        DateAndTimeInput(
            onDateChanged = { eventDate = it },
            onTimeChanged = { eventTime = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Done Button
        Button(
            onClick = {
                scope.launch {
                    try {
                        val formattedDate = LocalDate.parse(
                            eventDate,
                            DateTimeFormatter.ofPattern("MM/dd/yyyy")
                        )
                        val formattedTime = LocalTime.parse(
                            eventTime,
                            DateTimeFormatter.ofPattern("HH:mm")
                        )
                        eventDao.insertEvent(
                            Event(
                                eventName = eventName,
                                eventLocation = eventLocation,
                                eventNotes = eventNote,
                                eventDate = formattedDate.toString(),
                                eventTime = formattedTime.toString()
                            )
                        )
                        onStart()
                    } catch (e: Exception) {
                        // Handle the exception, show a message to the user
                        e.printStackTrace()
                    }
                }
            },
            enabled = isStartEnabled,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Done")
        }
    }
}

class MockEventDao : EventDao {
    override suspend fun getAllEvents(): List<Event> {
        return listOf() // Return an empty list or mock data
    }
    override suspend fun insertEvent(event: Event) {
        // Do nothing or log the event
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun NewEventScreenPreview() {
    val mockEventDao = MockEventDao()

    CountdowntimerappTheme {
        NewEventScreenLayout(
            onBack = { /* TODO: Implement back functionality */ },
            onStart = { /* TODO: Implement start functionality */ },
            isStartEnabled = true,
            eventDao = mockEventDao
        )
    }
}