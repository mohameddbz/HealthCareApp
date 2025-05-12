package com.example.projecttdm

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Log.d("FirebaseInit", "Firebase initialisé dans MyApp.kt")

    }
}