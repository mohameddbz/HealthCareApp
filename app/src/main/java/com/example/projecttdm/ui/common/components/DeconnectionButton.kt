package com.example.projecttdm.ui.common.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.projecttdm.data.model.auth.AuthResponse
import com.example.projecttdm.state.UiState
import com.example.projecttdm.ui.auth.AuthActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DeconnectionButton() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope() // Add this line

    Button(
        onClick = {
            scope.launch { // Use the coroutine scope
                withContext(Dispatchers.IO) {
                    val sharedPreferences =
                        context.getSharedPreferences("doctor_prefs", Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putBoolean("user_connected", false)
                        putString("auth_token", null)
                        putString("role", null)
                        apply()
                    }
                }

                // After logout, navigate to login screen
                val intent = Intent(context, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Deconnection")
    }
}