package com.example.countdown_timer_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.ContentAlpha
import androidx.compose.ui.text.input.KeyboardType
import com.example.countdown_timer_app.ui.theme.CountdowntimerappTheme

class NewEventScreen : ComponentActivity() {
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
                    NewEventScreenLayout(
                        onBack = { /* TODO: Implement back functionality */ },
                        onStart = { /* TODO: Implement start functionality */ },
                        isStartEnabled = true
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
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChanged,
        label = { Text(label) },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        modifier = modifier.fillMaxWidth(),
    )
}

/**
 * This composable function provides input fields for the user to enter the event details,
 * including the event name, optional notes, and event location. The location field allows the
 * user to enter any location, without prompting for a specific place. The notes field is optional
 * by default.
 */
@Composable
fun EventDetailsInput() {
    var eventName by remember { mutableStateOf("") }
    var eventNotes by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Please enter your event details.",
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )

        EditTextField(
            label = "Event Name",
            value = eventName,
            onValueChanged = { eventName = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        EditTextField(
            label = "Event Location",
            value = eventLocation,
            onValueChanged = { eventLocation = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        EditTextField(
            label = "Event Note (Optional)",
            value = eventNotes,
            onValueChanged = { eventNotes = it },
            singleLine = false,
            modifier = Modifier.height(100.dp)
        )
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
fun DateAndTimeInput() {
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date picker selection
            Column(modifier = Modifier.weight(1f)) {
                TextField(
                    value = selectedDate,
                    onValueChange = { selectedDate = it },
                    label = { Text("MM/DD/YYYY") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { /* TODO: Show DatePicker dialog and update selectedDate */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Date")
                }
            }

            // Time Picker Section
            Column(modifier = Modifier.weight(1f)) {
                TextField(
                    value = selectedTime,
                    onValueChange = { selectedTime = it },
                    label = { Text("HH:MM") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { /* TODO: Show TimePicker dialog and update selectedTime */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Time")
                }
            }
        }
    }
}

@Composable
fun NewEventScreenLayout(onBack: () -> Unit, onStart: () -> Unit, isStartEnabled: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        NewEventScreenAppBar(onBack, onStart, isStartEnabled)

        Spacer(modifier = Modifier.height(16.dp))

        EventDetailsInput()

        DateAndTimeInput()
    }
}

@Preview(showBackground = true)
@Composable
fun NewEventScreenPreview() {
    CountdowntimerappTheme {
        NewEventScreenLayout(
            onBack = { /* TODO: Implement back functionality */ },
            onStart = { /* TODO: Implement start functionality */ },
            isStartEnabled = true
        )
    }
}
