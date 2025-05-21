package com.example.projecttdm

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.projecttdm.data.db.scheduleSyncWork
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApp : Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
         scheduleSyncWork(this)
        Log.d("FirebaseInit", "Firebase initialisé dans MyApp.kt")
        val app = FirebaseApp.initializeApp(this)
        if (app == null) {
            Log.e("FirebaseInit", "FirebaseApp initialization failed")
        } else {
            Log.d("FirebaseInit", "Firebase initialized successfully")
        }
    }
}