package com.example.projecttdm

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("FirebaseInit", "Firebase initialisé dans MyApp.kt")
        val app = FirebaseApp.initializeApp(this)
        if (app == null) {
            Log.e("FirebaseInit", "FirebaseApp initialization failed")
        } else {
            Log.d("FirebaseInit", "Firebase initialized successfully")
        }
    }
}