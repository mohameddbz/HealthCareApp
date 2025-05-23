package com.example.projecttdm.ui.doctor

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.example.projecttdm.data.db.scheduleSyncWork
import com.example.projecttdm.theme.ProjectTDMTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DoctorActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Lancer le Worker uniquement depuis cette activité
        scheduleSyncWork(this)
        enableEdgeToEdge()
        setContent {
            ProjectTDMTheme{
               // DoctorNavigation()
                DoctorNavigation()
            }
        }
    }
}

