package com.example.projecttdm.ui.auth

import NotificationsScreen
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
import com.example.projecttdm.viewmodel.NotificationViewModel

@Composable
fun AuthNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = AuthRoutes.splash.route) {
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
                navController = navController
            )
        }


        composable(AuthRoutes.notificationScreen.route) {
            val notificationViewModel : NotificationViewModel = NotificationViewModel()
            notificationViewModel.getNotifications()
          NotificationsScreen(notificationViewModel)
        }


    }
}
