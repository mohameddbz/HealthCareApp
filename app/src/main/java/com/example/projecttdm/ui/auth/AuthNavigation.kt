package com.example.projecttdm.ui.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AuthNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = "login") {
        composable("login") {  }
        composable("register") {  }
    }
}
