package com.example.countdown_timer_app

import android.app.Application
import com.google.android.libraries.places.api.Places

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize the Places SDK
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyBW8G_bVq8Z3bygy0dW_BmHKHFK49t4Bu4")
        }
    }
}