package com.example.projecttdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.projecttdm.theme.ProjectTDMTheme
import com.example.projecttdm.ui.auth.AuthNavigation
import com.example.projecttdm.ui.doctor.DoctorNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectTDMTheme {
                var isLoggedIn by remember { mutableStateOf(false) }

                if (isLoggedIn) {
                    DoctorNavigation()
                } else {
                    AuthNavigation(
                        onLoginSuccess = {
                            isLoggedIn = true
                        }
                    )
                }
            }
        }
    }
}
