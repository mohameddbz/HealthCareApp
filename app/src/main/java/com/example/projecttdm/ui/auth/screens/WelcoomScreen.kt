package com.example.projecttdm.ui.auth.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.projecttdm.R
import com.example.projecttdm.ui.auth.AuthRoutes
import com.example.projecttdm.ui.auth.components.LoginButton
import com.example.projecttdm.ui.auth.components.SignUpButton
import com.example.projecttdm.ui.doctor.DoctorActivity
import com.example.projecttdm.ui.patient.PatientActivity

@Composable
fun WelcomeScreen(navController: NavHostController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.name),
            contentDescription = "Medics Logo",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Titre
        Text(
            text = "Let’s get started!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Description
        Text(
            text = "Login to enjoy the features we’ve provided, and stay healthy!",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Bouton "Login"
        LoginButton(onClick = {
            val userIsDoctor = false // change depending on your logic

            val intent = if (userIsDoctor) {
                Intent(context, DoctorActivity::class.java)
            } else {
                Intent(context, PatientActivity::class.java)
            }

            context.startActivity(intent)

            if (context is Activity) {
                context.finish()
            }
        })

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton "Sign Up"
        SignUpButton(onClick = {
            navController.navigate(AuthRoutes.loginScreen.route)
        })
    }
}
