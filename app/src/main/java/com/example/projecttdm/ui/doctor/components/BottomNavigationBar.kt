package com.example.projecttdm.ui.doctor.components

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.projecttdm.ui.doctor.DoctorRoutes

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Hide BottomNav for Camera screen
    if (currentDestination?.route == BottomNavItem.Camera.route) return

    BottomNavigation {
        bottomNavItems.forEach { item ->
            BottomNavigationItem(
                icon = {
                    if (item == BottomNavItem.Camera) {
                        // Centered camera icon can be styled differently if desired
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier.offset(y = (-4).dp) // Optional float effect
                        )
                    } else {
                        Icon(imageVector = item.icon, contentDescription = item.title)
                    }
                },
                label = { Text(text = item.title) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

// Define the BottomNav items
sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : BottomNavItem(DoctorRoutes.HomeScreen.route, Icons.Default.Home, "Accueil")
    object Favorites : BottomNavItem(DoctorRoutes.AppointmentValidationScreen.route, Icons.Default.Favorite, "Favoris")
    object Camera : BottomNavItem(DoctorRoutes.QrScanner.route, Icons.Default.CameraAlt, "Camera")
    object Profile : BottomNavItem(DoctorRoutes.QrScanner.route, Icons.Default.Person, "Profil")
    object Settings : BottomNavItem(DoctorRoutes.AppointmentOfWeek.route, Icons.Default.Settings, "Paramètres")
}

// Add items to the nav bar (camera in the center)
val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Favorites,
    BottomNavItem.Camera,   // Center item
    BottomNavItem.Profile,
    BottomNavItem.Settings
)
