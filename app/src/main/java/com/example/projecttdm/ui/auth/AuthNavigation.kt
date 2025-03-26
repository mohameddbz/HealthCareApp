package com.example.projecttdm.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projecttdm.ui.auth.screens.OnboardingScreen
import com.example.projecttdm.ui.auth.screens.SplashScreen
import com.example.projecttdm.R
import com.example.projecttdm.ui.auth.screens.WelcomeScreen

@Composable
fun AuthNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onFinish = { navController.navigate("onboarding") })
        }
        composable("onboarding") {
            OnboardingScreen(
                navController = navController,
                nextPage = "onboarding2", // Définis ici la page de destination
                doctorImage = R.drawable.doctor_image,
                titleText = "Consult only with a doctor you trust",
                indicatorColor1 = colorResource(id = R.color.bleu),
                indicatorColor2 = colorResource(id = R.color.bleu_200),
                indicatorColor3 = colorResource(id = R.color.bleu_200)
            )
        }
        composable("onboarding2") {
            OnboardingScreen(
                navController = navController,
                nextPage = "onboarding3",
                doctorImage = R.drawable.doctor_image2,
                titleText = "Find a lot of specialist doctors in one place",
                indicatorColor1 = colorResource(id = R.color.bleu_200),
                indicatorColor2 = colorResource(id = R.color.bleu),
                indicatorColor3 = colorResource(id = R.color.bleu_200)
            )
        }
        composable("onboarding3") {
            OnboardingScreen(
                navController = navController,
                nextPage = "welcomScreen",
                doctorImage = R.drawable.doctor_image3,
                titleText = "Get connect our Online Consultation",
                indicatorColor1 = colorResource(id = R.color.bleu_200),
                indicatorColor2 = colorResource(id = R.color.bleu_200),
                indicatorColor3 = colorResource(id = R.color.bleu)
            )
        }
        composable("welcomScreen") {
            WelcomeScreen(
                navController = navController
            )
        }
    }
}
