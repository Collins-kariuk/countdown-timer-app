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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.countdown_timer_app.ui.theme.CountdowntimerappTheme


class MainActivity : ComponentActivity() {
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
                    HomeScreenLayout( onAddEventClicked = { })
                }
            }
        }
    }
}

@Composable
fun HomeScreenAppBar() {
    // Start a row layout which arranges its children linearly horizontally.
    Row(
        // Apply modifiers to the Row for layout configurations:
        modifier = Modifier
            .fillMaxWidth()  // Make the Row fill the maximum available width.
            // The primary color is the color displayed most frequently across your app’s screens
            // and components.
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp, vertical = 8.dp), // Apply padding to the Row.
        // Align children vertically to the center of the row.
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Space children evenly
    ) {
        Text(
            text = stringResource(R.string.App_name),
            style = MaterialTheme.typography.titleLarge, // Apply typography style from Material theme.
            // Text color for readability on primary background.
            color = MaterialTheme.colorScheme.onPrimary,
            // Flex modifier to make the text occupy the remaining space.
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center // Center the text horizontally within its container.
        )

        Spacer(modifier = Modifier.width(8.dp))  // Adds space between the icon and the text.

        IconButton(
            // Set the action to perform when the IconButton is clicked.
            onClick = { /* TODO: Implement search functionality */ },
            // Apply a size modifier to set the height and width of the IconButton.
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                // Use a predefined icon from the material icons set, specifically the search icon.
                imageVector = Icons.Filled.Search,
                // Provide a content description for accessibility tools like screen readers.
                contentDescription = "Search",
                // Set the icon color to white for contrast against the blue background.
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun NewEventScreen(onAddEventClicked: () -> Unit) {
    // Defines a Button composable that users can interact with.
    Button(
        // Lambda expression that is executed when the Button is clicked.
        onClick = { onAddEventClicked() },
        // Modifier to control the size and layout of the Button.
        modifier = Modifier
            // The Button fills 50% of the maximum width of its parent.
            .fillMaxWidth(0.5f)
            .height(50.dp), // Sets the height of the Button to 50 density-independent pixels.
        // Sets the shape of the Button corners to be rounded with an 8 dp radius.
        shape = RoundedCornerShape(8.dp),
        // Adds a border around the Button with a width of 1 dp and black color.
        border = BorderStroke(1.dp, Color.Black)
    ) {
        // Text composable inside the Button to display the button's label.
        Text(
            // Sets the text to display on the Button.
            text = stringResource(R.string.New_event_String),
            // Sets the font size of the text to 18 scalable pixels (sp).
            fontSize = 18.sp,
            // Sets the color of the text to black.
            color = Color.Black
        )
    }
}

@Composable
// This function constructs the layout for the home screen of the application.
fun HomeScreenLayout(onAddEventClicked: () -> Unit) {
    // Column composable arranges its children vertically.
    Column(
        // Modifier is applied to the Column to adjust its layout parameters.
        modifier = Modifier
            .fillMaxSize()  // Makes the Column fill the entire available space of its parent.
            // Adds padding of 16 density-independent pixels around the Column's content.
            .padding(16.dp),
        // Aligns the children of the Column horizontally to the center.
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Includes the AppBar at the top of the Home Screen by calling another composable function.
        HomeScreenAppBar()  // AppBar includes a title and a search icon.

        // Adds a vertical space of 48 density-independent pixels between the AppBar and the next
        // component.
        Spacer(modifier = Modifier.height(48.dp))

        NewEventScreen(onAddEventClicked)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CountdowntimerappTheme {
        HomeScreenLayout(onAddEventClicked = { })
    }
}