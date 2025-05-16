package com.example.projecttdm

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.projecttdm.data.endpoint.ApiClient
import com.example.projecttdm.theme.ProjectTDMTheme
import com.example.projecttdm.ui.auth.AuthActivity
import com.example.projecttdm.ui.auth.AuthNavigation
import com.example.projecttdm.ui.doctor.DoctorActivity
import com.example.projecttdm.ui.doctor.DoctorNavigation
import com.example.projecttdm.ui.patient.PatientActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable edge-to-edge display

        // Show a splash screen while checking authentication
        setContent {
            ProjectTDMTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        // Check authentication status in a background thread
        lifecycleScope.launch(Dispatchers.IO) {
            // Get authentication data from SharedPreferences
            val sharedPreferences = getSharedPreferences("doctor_prefs", MODE_PRIVATE)
            val token = sharedPreferences.getString("auth_token", null)
            val userConnected = sharedPreferences.getBoolean("user_connected", false)
            val role = sharedPreferences.getString("role",null)


            // Switch back to main thread to navigate
            withContext(Dispatchers.Main) {
                // Navigate to appropriate screen based on authentication status
                if (token.isNullOrEmpty() || !userConnected) {
                    navigateToAuth()
                } else {
                    ApiClient.setTokenProvider { token }
                    if(role =="doctor") {
                        navigateToDoctor()
                    }else if (role == "patient") {
                        navigateToPatient()
                    }

                }
            }
        }
    }

    private fun navigateToAuth() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun navigateToDoctor(){
        val intent = Intent(this,DoctorActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToPatient() {
        val intent = Intent(this, PatientActivity::class.java)
        startActivity(intent)
        finish()
    }
}


/*

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        

        setContent {
            ProjectTDMTheme {
                DoctorNavigation(
                )
            }
        }
    }
}


//        // Check authentication status in a background thread
//        lifecycleScope.launch(Dispatchers.IO) {
//            // Get authentication data from SharedPreferences
//            val sharedPreferences = getSharedPreferences("doctor_prefs", MODE_PRIVATE)
//            val token = sharedPreferences.getString("auth_token", null)
//            val userConnected = sharedPreferences.getBoolean("user_connected", false)
//            val role = sharedPreferences.getString("role",null)
//
//
//            // Switch back to main thread to navigate
//            withContext(Dispatchers.Main) {
//                // Navigate to appropriate screen based on authentication status
//                if (token.isNullOrEmpty() || !userConnected) {
//                    navigateToAuth()
//                } else {
//                    ApiClient.setTokenProvider { token }
//                    if(role =="doctor") {
//                        navigateToDoctor()
//                    }else if (role == "patient") {
//                        navigateToPatient()
//                    }
//
//                }
//            }
//        }
//    }
//
//    private fun navigateToAuth() {
//        val intent = Intent(this, AuthActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//    private fun navigateToDoctor(){
//        val intent = Intent(this,DoctorActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//
//    private fun navigateToPatient() {
//        val intent = Intent(this, PatientActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//}
    *
 */