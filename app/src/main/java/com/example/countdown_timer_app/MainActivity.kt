package com.example.countdown_timer_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.countdown_timer_app.ui.theme.CountdowntimerappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CountdowntimerappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreenLayout( onAddEventClicked = { })
                }
            }
        }
    }
}

@Composable
// Defines a composable function that creates the home screen layout.
fun HomeScreenLayout(onAddEventClicked: () -> Unit) {
    // Column composable to layout items vertically.
    // The column fills the maximum size of the parent and applies padding of 16 dp around its
    // contents.
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        // Aligns children horizontally to the center.
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Search button icon
            IconButton(
                onClick = { /* TODO: Implement search functionality */ },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }

            // Adds space between the search icon and the app name
            Spacer(modifier = Modifier.width(8.dp))

            // Text composable for displaying the app name.
            // It is styled with specific font size and weight.
            Text(
                text = "Countdown Timer App", // The text to display, which is the app's name.
                fontSize = 24.sp, // Sets the size of the font.
                fontWeight = FontWeight.Bold, // Makes the font bold.
                modifier = Modifier.weight(1f).align(CenterVertically),
                textAlign = TextAlign.Center // Centers the text within the Text composable.
            )
            // Placeholder for right alignment
            Spacer(modifier = Modifier.width(24.dp))
        }

        // Spacer composable to create empty space between elements, here it is 48 dp tall.
        Spacer(modifier = Modifier.height(48.dp))

        // Button composable that users can click to trigger an action.
        Button(
            // Lambda function that is called when the button is clicked.
            onClick = { onAddEventClicked() },
            modifier = Modifier
                // Makes the Button fill half of the maximum width available.
                .fillMaxWidth(0.5f)
                // Sets the height of the Button to 50 dp.
                .height(50.dp),
            // Rounds the corners of the button.
            shape = RoundedCornerShape(8.dp),
            // Defines a black border around the button.
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Text(
                text = "Add a new event", // Text to display on the button.
                fontSize = 18.sp, // Font size for the text inside the button.
                color = Color.Black // Text color.
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CountdowntimerappTheme {
        HomeScreenLayout(onAddEventClicked = { })
    }
}