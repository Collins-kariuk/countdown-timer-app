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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.ImeAction
import androidx.wear.compose.material.ContentAlpha
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
    private lateinit var eventDao: EventDao

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Makes the app content extend into window insets areas like status and navigation bars.
        enableEdgeToEdge()

        // Initialize the database and get the DAO
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "event-database"
        ).build()
        eventDao = db.eventDao()

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
                        onBack = { /* TODO: Implement back functionality */ },
                        onStart = { finish() }, // Navigate back to home
                        isStartEnabled = true,
                        eventDao = eventDao
                    )
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
fun EditTextField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        value = value,
        onValueChange = onValueChanged,
        label = { Text(label) },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun DateAndTimeInput(
    selectedDate: String,
    onDateChanged: (String) -> Unit,
    selectedTime: String,
    onTimeChanged: (String) -> Unit
) {
    var selectedPeriod by remember { mutableStateOf("AM") }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Date picker dialog
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val date = "${month + 1}/$dayOfMonth/$year"
            onDateChanged(date)
        },
        2024, 6, 13
    )

    // Time picker dialog
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            val time = String.format("%02d:%02d", hourOfDay, minute)
            onTimeChanged(time)
        },
        12, 0, false
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date picker button
            Button(
                onClick = {
                    datePickerDialog.show()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Set Date")
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Time Picker Section
            Row(modifier = Modifier.weight(1f)) {
                Button(
                    onClick = {
                        timePickerDialog.show()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Set Time")
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
        NewEventScreenAppBar(onBack, {
            scope.launch {
                try {
                    val formattedDate = LocalDate.parse(eventDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                    val formattedTime = LocalTime.parse(eventTime, DateTimeFormatter.ofPattern("HH:mm"))
                    eventDao.insertEvent(Event(
                        eventName = eventName,
                        eventLocation = "",
                        eventNotes = eventNote,
                        eventDate = formattedDate.toString(),
                        eventTime = formattedTime.toString()
                    ))
                    onStart()
                } catch (e: Exception) {
                    // Handle the exception, show a message to the user
                    e.printStackTrace()
                }
            }
        }, isStartEnabled)

        Spacer(modifier = Modifier.height(16.dp))

        // Other input fields for event details
        EventDetailsInput(
            eventName = eventName,
            onEventNameChange = { eventName = it },
            eventLocation = "",
            onEventLocationChange = { /* handle location change if necessary */ },
            eventNotes = eventNote,
            onEventNotesChange = { eventNote = it }
        )

        DateAndTimeInput(
            selectedDate = eventDate,
            onDateChanged = { eventDate = it },
            selectedTime = eventTime,
            onTimeChanged = { eventTime = it }
        )
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
