package com.example.projecttdm.ui.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projecttdm.ui.auth.Screen.FillProfileScreen
import com.example.projecttdm.ui.auth.Screen.LoginScreen
import com.example.projecttdm.ui.auth.Screen.SignInScreen

@Composable
fun AuthNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = "profile") {
        composable("login") { LoginScreen() }
        composable("register") { SignInScreen() }
        composable("profile"){FillProfileScreen()}
    }
}
