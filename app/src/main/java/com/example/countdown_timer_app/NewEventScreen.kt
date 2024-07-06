package com.example.countdown_timer_app

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.countdown_timer_app.ui.theme.CountdowntimerappTheme
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class NewEventScreen : ComponentActivity() {
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
        viewModel = ViewModelProvider(this, EventViewModelFactory(eventDao))[EventViewModel::class.java]

        setContent {
            CountdowntimerappTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    NewEventScreenLayout(
                        viewModel = viewModel,
                        onBack = { finish() },
                        onStart = {
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                            finish()
                        },
                        eventDao = eventDao
                    )
                }
            }
        }
    }
}

@Composable
fun NewEventScreenAppBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
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
    onDateChanged: (String) -> Unit,
    onTimeChanged: (String) -> Unit
) {
    val context = LocalContext.current
    var dateButtonText by remember { mutableStateOf("Set Date") }
    var timeButtonText by remember { mutableStateOf("Set Time") }

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val date = "${String.format("%02d", month + 1)}/${String.format("%02d", dayOfMonth)}/$year"
            onDateChanged(date)
            dateButtonText = date
        },
        2024, 6, 22
    ).apply {
        datePicker.minDate = Calendar.getInstance().timeInMillis
    }

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            val time = String.format("%02d:%02d", hourOfDay, minute)
            onTimeChanged(time)
            timeButtonText = time
        },
        12, 0, false
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { datePickerDialog.show() },
                modifier = Modifier.weight(1f)
            ) {
                Text(dateButtonText)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Row(modifier = Modifier.weight(1f)) {
                Button(
                    onClick = { timePickerDialog.show() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(timeButtonText)
                }
            }
        }
    }
}

@Composable
fun EventDetailsInput(
    eventName: String,
    onEventNameChange: (String) -> Unit,
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

        EditTextField(
            label = stringResource(R.string.event_name),
            value = eventName,
            onValueChanged = onEventNameChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LocationAutoCompleteField(
            label = stringResource(R.string.event_location),
            onLocationSelected = onEventLocationChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        EditTextField(
            label = stringResource(R.string.optional_event_note),
            value = eventNotes,
            onValueChanged = onEventNotesChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = false,
            modifier = Modifier.height(100.dp)
        )
    }
}

@Composable
fun LocationAutoCompleteField(
    label: String,
    onLocationSelected: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf(listOf<AutocompletePrediction>()) }
    val context = LocalContext.current
    val placesClient: PlacesClient = remember { Places.createClient(context) }
    val token = remember { AutocompleteSessionToken.newInstance() }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                fetchSuggestions(context, placesClient, token, it) { predictions ->
                    suggestions = predictions
                }
            },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth()
        )

        if (suggestions.isNotEmpty()) {
            DropdownMenu(
                expanded = suggestions.isNotEmpty(),
                onDismissRequest = { suggestions = emptyList() },
                modifier = Modifier.fillMaxWidth()
            ) {
                suggestions.forEach { suggestion ->
                    DropdownMenuItem(
                        text = { Text(suggestion.getFullText(null).toString()) },
                        onClick = {
                            query = suggestion.getFullText(null).toString()
                            onLocationSelected(query)
                            suggestions = emptyList()
                        }
                    )
                }
            }
        }
    }
}

fun fetchSuggestions(
    context: Context,
    placesClient: PlacesClient,
    token: AutocompleteSessionToken,
    query: String,
    callback: (List<AutocompletePrediction>) -> Unit
) {
    val request = FindAutocompletePredictionsRequest.builder()
        .setSessionToken(token)
        .setQuery(query)
        .build()

    placesClient.findAutocompletePredictions(request)
        .addOnSuccessListener { response ->
            callback(response.autocompletePredictions)
        }
        .addOnFailureListener { exception ->
        Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewEventScreenLayout(
    viewModel: EventViewModel,
    onBack: () -> Unit,
    onStart: () -> Unit,
    eventDao: EventDao
) {
    val scope = rememberCoroutineScope()
    var eventName by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var eventTime by remember { mutableStateOf("") }
    var eventNote by remember { mutableStateOf("") }

    val isFormValid by remember {
        derivedStateOf {
            eventName.isNotBlank() && eventDate.isNotBlank() && eventTime.isNotBlank()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        NewEventScreenAppBar(onBack)

        Spacer(modifier = Modifier.height(16.dp))

        EventDetailsInput(
            eventName = eventName,
            onEventNameChange = { eventName = it },
            onEventLocationChange = { eventLocation = it },
            eventNotes = eventNote,
            onEventNotesChange = { eventNote = it }
        )
        DateAndTimeInput(
            onDateChanged = { eventDate = it },
            onTimeChanged = { eventTime = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (isFormValid) {
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
                            e.printStackTrace()
                        }
                    }
                }
            },
            enabled = isFormValid,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Done")
        }
    }
}

class MockEventDao : EventDao {
    private val events = mutableListOf<Event>()

    override suspend fun getAllEvents(): List<Event> {
        return events
    }

    override suspend fun insertEvent(event: Event) {
        events.add(event)
    }

    override suspend fun deleteEvent(event: Event) {
        events.remove(event)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun NewEventScreenPreview() {
    val mockEventDao = MockEventDao()
    val context = LocalContext.current
    CountdowntimerappTheme {
        NewEventScreenLayout(
            viewModel = viewModel,
            onBack = { /* TODO: Implement back functionality */ },
            onStart = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            },
            eventDao = mockEventDao
        )
    }
}