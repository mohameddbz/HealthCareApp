package com.example.projecttdm.ui.auth.screens

import NotificationsScreen
import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
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
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Medics Logo",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Titre
        Text(
            text = "Let’s get started!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Description
        Text(
            text = "Login to enjoy the features we’ve provided, and stay healthy!",
            fontSize = 14.sp,
            color = colorResource(id = R.color.gray),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Bouton "Login"
        LoginButton(onClick = {
          // khawti hedi tetebdl w tweli f page te3 login
            val userIsDoctor = false // ou false si patient

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
        SignUpButton (onClick = { })
    }
}

