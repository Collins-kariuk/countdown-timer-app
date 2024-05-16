package com.example.countdown_timer_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

                    )
                }
            }
        }
    }
}

@Composable
fun NewEventScreenAppBar() {

}

@Composable
fun NewEventScreenLayout() {

}

@Preview(showBackground = true)
@Composable
fun NewEventScreenPreview() {
    CountdowntimerappTheme {
    }
}