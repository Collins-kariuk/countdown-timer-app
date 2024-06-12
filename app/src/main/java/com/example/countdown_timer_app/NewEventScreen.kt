package com.example.countdown_timer_app

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.ImeAction
import androidx.wear.compose.material.ContentAlpha
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.time.LocalDate
import java.time.LocalTime
import androidx.room.TypeConverter
import java.time.format.DateTimeFormatter
import androidx.room.Room
import androidx.compose.runtime.rememberCoroutineScope
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

class LocalDateConverter {
    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromString(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it, formatter) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun dateToString(date: LocalDate?): String? {
        return date?.format(formatter)
    }
}

class LocalTimeConverter {
    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ISO_LOCAL_TIME

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromString(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it, formatter) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun timeToString(time: LocalTime?): String? {
        return time?.format(formatter)
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

fun dateVisualTransformation(): VisualTransformation {
    return VisualTransformation { text ->
        val trimmed = text.text.take(8) // Ensure text is at most 8 characters long
        val out = StringBuilder()

        // Build the transformed text with slashes at the correct positions
        for (i in trimmed.indices) {
            out.append(trimmed[i])
            if (i == 1 || i == 3) out.append('/')
        }

        // Calculate the offset mapping for cursor position
        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 1 -> offset
                    offset <= 3 -> offset + 1
                    offset <= 6 -> offset + 2
                    else -> minOf(offset + 2, out.length)
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    offset <= 5 -> offset - 1
                    else -> offset - 2
                }
            }
        }

        // Return the transformed text and offset mapping
        TransformedText(text = AnnotatedString(out.toString()), offsetMapping = offsetTranslator)
    }
}

fun timeVisualTransformation(): VisualTransformation {
    return VisualTransformation { text ->
        val trimmed = text.text.take(4) // Ensure text is at most 4 characters long
        val out = StringBuilder()

        // Build the transformed text with colon at the correct position
        for (i in trimmed.indices) {
            out.append(trimmed[i])
            if (i == 1) out.append(':')
        }

        // Calculate the offset mapping for cursor position
        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 1 -> offset
                    else -> minOf(offset + 1, out.length)
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    else -> offset - 1
                }
            }
        }

        // Return the transformed text and offset mapping
        TransformedText(text = AnnotatedString(out.toString()), offsetMapping = offsetTranslator)
    }
}

/**
 * This composable function provides date and time pickers for the user to select the event's date
 * and time. The date picker is on the left side, and the time picker is on the right side.
 *
 * For the date picker, users have two input options:
 * - A text field at the top to manually enter a date in the format MM/DD/YYYY, which only accepts
 *   date inputs.
 *
 * For the time picker, users also have two input options:
 * - A text field at the top to manually enter a time in the format HH:MM, which only accepts time
 *   inputs.
 */
@Composable
fun DateAndTimeInput(
    selectedDate: String,
    onDateChanged: (String) -> Unit,
    selectedTime: String,
    onTimeChanged: (String) -> Unit
) {
    var selectedPeriod by remember { mutableStateOf("AM") }
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date picker selection
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Please enter the event date:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                EditTextField(
                    label = "MM/DD/YYYY",
                    value = selectedDate,
                    onValueChanged = onDateChanged,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = dateVisualTransformation()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Time Picker Section
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Please enter the event time:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    EditTextField(
                        label = "HH:MM",
                        value = selectedTime,
                        onValueChanged = onTimeChanged,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = timeVisualTransformation(),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box {
                        Button(onClick = { expanded = true }) {
                            Text(selectedPeriod)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("AM") },
                                onClick = {
                                    selectedPeriod = "AM"
                                    expanded = false
                                })

                            DropdownMenuItem(
                                text = { Text("PM") },
                                onClick = {
                                    selectedPeriod = "PM"
                                    expanded = false
                                })
                        }
                    }
                }
            }
        }
    }
}

/**
 * This composable function provides input fields for the user to enter the event details,
 * including the event name, optional notes, and event location. The location field allows the
 * user to enter any location, without prompting for a specific place. The notes field is optional
 * by default.
 */
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
