package com.example.projecttdm.ui.auth

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projecttdm.ui.auth.screens.OnboardingScreen
import com.example.projecttdm.ui.auth.screens.SplashScreen
import com.example.projecttdm.R
import com.example.projecttdm.ui.auth.screens.FillProfileScreen
import com.example.projecttdm.ui.auth.screens.LoginScreen
import com.example.projecttdm.ui.auth.screens.SignInScreen
import com.example.projecttdm.ui.auth.screens.WelcomeScreen
import com.example.projecttdm.viewmodel.AuthViewModel

@Composable
fun AuthNavigation(navController: NavHostController = rememberNavController(),
                   onLoginSuccess: () -> Unit) {

    // Obtenir le contexte actuel
    val context = LocalContext.current

    // Obtenir l'instance Application depuis le contexte
    val application = context.applicationContext as Application

    // Créer une factory pour votre AndroidViewModel
    val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    // Utiliser la factory avec viewModel()
    val authViewModel: AuthViewModel = viewModel(factory = factory)
   // val authViewModel =  ViewModelProvider(this).get(AuthViewModel::class.java);

    NavHost(navController, startDestination = AuthRoutes.loginScreen.route) {
        composable(AuthRoutes.splash.route) {
            SplashScreen(onFinish = { navController.navigate(AuthRoutes.onboarding.route) })
        }
        composable(AuthRoutes.onboarding.route) {
            OnboardingScreen(
                navController = navController,
                nextPage = AuthRoutes.onboarding2.route, // Définis ici la page de destination
                doctorImage = R.drawable.doctor_image,
                titleText = "Consult only with a doctor you trust",
                indicatorColor1 = colorResource(id = R.color.bleu),
                indicatorColor2 = colorResource(id = R.color.bleu_200),
                indicatorColor3 = colorResource(id = R.color.bleu_200)
            )
        }
        composable(AuthRoutes.onboarding2.route) {
            OnboardingScreen(
                navController = navController,
                nextPage = AuthRoutes.onboarding3.route,
                doctorImage = R.drawable.doctor_image2,
                titleText = "Find a lot of specialist doctors in one place",
                indicatorColor1 = colorResource(id = R.color.bleu_200),
                indicatorColor2 = colorResource(id = R.color.bleu),
                indicatorColor3 = colorResource(id = R.color.bleu_200)
            )
        }
        composable(AuthRoutes.onboarding3.route) {
            OnboardingScreen(
                navController = navController,
                nextPage = AuthRoutes.welcomScreen.route,
                doctorImage = R.drawable.doctor_image3,
                titleText = "Get connect our Online Consultation",
                indicatorColor1 = colorResource(id = R.color.bleu_200),
                indicatorColor2 = colorResource(id = R.color.bleu_200),
                indicatorColor3 = colorResource(id = R.color.bleu)
            )
        }
        composable(AuthRoutes.welcomScreen.route) {
            WelcomeScreen(
                navController = navController,
            )
        }
        composable(AuthRoutes.loginScreen.route) {
            LoginScreen(navController = navController,authViewModel)
        }
        composable(AuthRoutes.registerScreen.route) {
            SignInScreen(navController = navController,authViewModel)
        }
        composable(AuthRoutes.profileScreen.route) {
            FillProfileScreen(authViewModel)
        }
    }

}



