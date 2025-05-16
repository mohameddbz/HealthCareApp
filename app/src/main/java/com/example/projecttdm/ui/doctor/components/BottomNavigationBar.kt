package com.example.projecttdm.ui.doctor.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.projecttdm.ui.doctor.DoctorRoutes


// Composable de la barre de navigation
@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation {
        // Obtenir l'entrée de navigation actuelle
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        // Afficher tous les items dans la barre de navigation
        bottomNavItems.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        // Pour éviter d'empiler plusieurs fois la même destination
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Launchsingleto pour éviter plusieurs copies de la même destination
                        launchSingleTop = true
                        // Restaurer l'état lors de la navigation
                        restoreState = true
                    }
                }
            )
        }
    }
}



// Définition des items de la barre de navigation
sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : BottomNavItem(DoctorRoutes.HomeScreen.route, Icons.Default.Home, "Accueil")
    object Favorites : BottomNavItem(DoctorRoutes.AppointmentValidationScreen.route, Icons.Default.Favorite, "Favoris")
    object Profile : BottomNavItem(DoctorRoutes.HomeScreen.route, Icons.Default.Person, "Profil")
    object Settings : BottomNavItem(DoctorRoutes.AppointmentOfWeek.createRoute("1"), Icons.Default.Settings, "Paramètres")
}

// Les éléments de navigation disponibles
val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Favorites,
    BottomNavItem.Profile,
    BottomNavItem.Settings
)
