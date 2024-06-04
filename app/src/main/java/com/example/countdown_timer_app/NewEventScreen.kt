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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.ImeAction
import androidx.wear.compose.material.ContentAlpha
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.room.Entity
import androidx.room.PrimaryKey
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
            text = stringResource(R.string.enter_event_details),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )

        // Event Name
        EditTextField(
            label = stringResource(R.string.event_name),
            value = eventName,
            onValueChanged = { eventName = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Event Location
        EditTextField(
            label = stringResource(R.string.event_location),
            value = eventLocation,
            onValueChanged = { eventLocation = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Event Notes (Optional)
        EditTextField(
            label = stringResource(R.string.optional_event_note),
            value = eventNotes,
            onValueChanged = { eventNotes = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next),
            singleLine = false,
            modifier = Modifier.height(100.dp)
        )
    }
}

@Composable
fun NewEventScreenLayout(
    onBack: () -> Unit,
    onStart: () -> Unit,
    isStartEnabled: Boolean
) {
    // Scaffold composable provides a layout structure with a top bar and a content area
    Scaffold(
        topBar = {
            NewEventScreenAppBar(
                onBack = onBack,
                onStart = onStart,
                isStartEnabled = isStartEnabled
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
                EventDetailsInput()
                DateAndTimeInput()
            }
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

// Defines a function that returns a VisualTransformation for a date input field.
fun dateVisualTransformation(): VisualTransformation {
    // Returns a VisualTransformation object that transforms the input text visually.
    return VisualTransformation { text ->
        // Trims the input text to a maximum length of 8 characters. This ensures the format
        // MM/DD/YYYY.
        val trimmed = if (text.text.length >= 8) text.text.substring(0..7)
                    else text.text

        // StringBuilder is used to build the transformed text efficiently.
        val out = StringBuilder()

        // Loop through each character in the trimmed text.
        for (i in trimmed.indices) {
            // Append the current character to the StringBuilder.
            out.append(trimmed[i])
            // Insert a '/' after the second and fourth characters to match the MM/DD/YYYY format.
            if (i == 1 || i == 3) out.append('/')
        }

        // Defines an offset mapping to correctly map cursor position between original and
        // transformed text.
        val offsetTranslator = object : OffsetMapping {
            // Maps the cursor position from original to transformed text.
            override fun originalToTransformed(offset: Int): Int {
                return if (offset <= 1) {
                    offset // Before or at the first '/'
                } else if (offset <= 3) {
                    offset + 1 // Between the first '/' and second '/'
                } else {
                    offset + 2 // After the second '/'
                }
            }

            // Maps the cursor position from transformed to original text.
            override fun transformedToOriginal(offset: Int): Int {
                return if (offset <= 2) {
                    offset // Before or at the first '/'
                } else if (offset <= 5) {
                    offset - 1 // Between the first '/' and second '/'
                } else {
                    offset - 2 // After the second '/'
                }
            }
        }

        // Returns a TransformedText object with the transformed text and the offset mapping.
        TransformedText(text = AnnotatedString(out.toString()), offsetMapping = offsetTranslator)
    }
}

// Defines a function that returns a VisualTransformation for a time input field.
fun timeVisualTransformation(): VisualTransformation {
    // Returns a VisualTransformation object that transforms the input text visually.
    return VisualTransformation { text ->
        // Trims the input text to a maximum length of 4 characters.
        val trimmed = if (text.text.length >= 4) text.text.substring(0..3)
                    else text.text

        // StringBuilder is used to build the transformed text efficiently.
        val out = StringBuilder()

        // Loop through each character in the trimmed text.
        for (i in trimmed.indices) {
            // Append the current character to the StringBuilder.
            out.append(trimmed[i])
            // Insert a ':' after the second character.
            if (i == 1) out.append(':')
        }

        // Defines an offset mapping to correctly map cursor position between original and
        // transformed text.
        val offsetTranslator = object : OffsetMapping {
            // Maps the cursor position from original to transformed text.
            override fun originalToTransformed(offset: Int): Int {
                return if (offset <= 1) {
                    offset // Before or at the ':'
                } else {
                    offset + 1 // After the ':'
                }
            }

            // Maps the cursor position from transformed to original text.
            override fun transformedToOriginal(offset: Int): Int {
                return if (offset <= 2) {
                    offset // Before or at the ':'
                } else {
                    offset - 1 // After the ':'
                }
            }
        }

        // Returns a TransformedText object with the transformed text and the offset mapping.
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
fun DateAndTimeInput() {
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
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
                    onValueChanged = { selectedDate = it },
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
                        onValueChanged = { selectedTime = it },
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
                                onClick = { selectedPeriod = "AM"
                                            expanded = false })

                            DropdownMenuItem(
                                text = { Text("PM") },
                                onClick = { selectedPeriod = "PM"
                                            expanded = false })
                        }
                    }
                }
            }
        }
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
