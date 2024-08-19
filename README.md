# Countdown Timer App

**Countdown Timer** is an Android application designed to help users track the time remaining until important events. The app allows users to create and manage countdown timers for various events, providing a clear and customizable interface to keep track of upcoming deadlines, special occasions, or personal goals.

## Status.
The project is nearly complete, but I've decided to temporarily pause development to focus on reviewing quantum mechanics. I plan to return and finish the app soon.

## Features

- **Create Countdown Events**: Users can create countdown events by specifying the event name, date, time, notes, and location.
- **Event Management**: Edit or delete existing countdown events with ease.
- **Real-Time Countdown**: The app provides a live countdown display for each event, updating every second.

## Tech Stack

- **Kotlin**: The primary programming language used for the Android app.
- **Jetpack Compose**: Used for building the UI, providing a modern, declarative approach to creating the user interface.
- **Room Database**: Manages local data storage, allowing users to save, retrieve, update, and delete countdown events.
- **ViewModel**: Manages UI-related data in a lifecycle-conscious way, ensuring data is retained across configuration changes.
- **Coroutines**: Provides a streamlined way to handle asynchronous tasks, like loading events from the database or updating countdown timers.

## Project Structure

- **`com.example.countdown_timer_app`**: The main package containing all the source code.
  - **`data`**: Contains the `Event` data class, representing the countdown events stored in the database.
  - **`ui`**: Contains UI-related components like the event detail screen and app bar.
  - **`viewmodel`**: Contains the `EventViewModel` and `EventViewModelFactory`, which handle the business logic and interaction with the database.
  - **`database`**: Contains the Room database setup and DAO interface for accessing event data.

## Installation

To run the Countdown Timer app on your local machine:

1. Clone the repository:
   ```bash
   git clone https://github.com/Collins-kariuk/countdown-timer.git
   ```
2. Open the project in Android Studio.
3. Build the project and run it on an emulator or a physical device.

## Usage

- Launch the app to view the list of active countdown events.
- Tap the "+" button to add a new event by entering the required details.
- Select an event to view its details and the live countdown.
- Edit or delete events directly from the detail view.

## Contributing

Contributions are welcome! If you'd like to contribute, please follow these steps:

1. Fork the repository.
2. Create a new branch.
3. Make your changes and commit them.
4. Push to the branch.
5. Open a pull request.

## License

This project is licensed under the MIT License.

## Contact

For any questions or suggestions, feel free to contact the project maintainer, Collins Munene Kariuki.
