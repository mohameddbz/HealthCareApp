package com.example.projecttdm.ui.patient

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.projecttdm.R
import com.example.projecttdm.ui.patient.Components.DoctorSpecialitySection
import com.example.projecttdm.ui.patient.Components.MedicalCheckBanner
import  com.example.projecttdm.ui.patient.Components.SearchBar


// Classe pour gérer les informations de taille d'écran
class WindowSize(
    val width: WindowType,
    val height: WindowType
)

// Énumération pour les différents types de fenêtres
enum class WindowType { Compact, Medium, Expanded }

// Extension function pour déterminer le type de fenêtre en fonction de la taille
fun getWindowType(size: Int): WindowType = when {
    size < 600 -> WindowType.Compact
    size < 840 -> WindowType.Medium
    else -> WindowType.Expanded
}

@Composable
fun HomeScreen(navController :NavHostController) {
    var searchQuery by remember { mutableStateOf("") }

    val windowSize: WindowSize = calculateWindowSize()
    Scaffold(
        topBar = {
            TopAppBar(onNotificationClick = {
                navController.navigate(PatientRoutes.NotificationScreen.route)
            })
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)
        ) {

            // Search Bar
            SearchBar(
                windowSize = windowSize,
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { query ->
                    // Action à effectuer lors de la recherche
                    println("Recherche: $query")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Medical Check Banner
            MedicalCheckBanner(windowSize)

            Spacer(modifier = Modifier.height(16.dp))

            // Doctor Speciality Section
            DoctorSpecialitySection(windowSize)

            Spacer(modifier = Modifier.height(16.dp))

            // Top Doctors Section
            TopDoctorsSection(windowSize)

            Spacer(modifier = Modifier.height(80.dp)) // Space for bottom navigation

        }
    }


}

@Composable
fun calculateWindowSize(): WindowSize {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    return WindowSize(
        width = getWindowType(screenWidth),
        height = getWindowType(screenHeight)
    )
}

@Composable
fun TopAppBar(onNotificationClick :()-> Unit) {
    val windowSize: WindowSize = calculateWindowSize()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Image
        Image(
            painter = painterResource(id = R.drawable.doctor_image2),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(
                    when (windowSize.width) {
                        WindowType.Compact -> 40.dp
                        else -> 48.dp
                    }
                )
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Welcome Text
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Good Morning ",
                    fontSize = when (windowSize.width) {
                        WindowType.Compact -> 14.sp
                        else -> 16.sp
                    },
                    color = Color.Gray
                )
                Text(
                    text = "👋",
                    fontSize = when (windowSize.width) {
                        WindowType.Compact -> 14.sp
                        else -> 16.sp
                    }
                )
            }
            Text(
                text = "Andrew Ainsley",
                fontSize = when (windowSize.width) {
                    WindowType.Compact -> 16.sp
                    else -> 18.sp
                },
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // Notification Icon
        IconButton(onClick = onNotificationClick) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                tint = Color.Black,
                modifier = Modifier.size(when (windowSize.width) {
                    WindowType.Compact -> 24.dp
                    else -> 28.dp
                })
            )
        }

        // Favorite Icon
        IconButton(onClick = { /* Favorite action */ }) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorites",
                tint = Color.Black,
                modifier = Modifier.size(when (windowSize.width) {
                    WindowType.Compact -> 24.dp
                    else -> 28.dp
                })
            )
        }
    }
}


@Composable
fun TopDoctorsSection(windowSize: WindowSize) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Top Doctors",
                color = Color.Black,
                fontSize = when (windowSize.width) {
                    WindowType.Compact -> 18.sp
                    else -> 20.sp
                },
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "See All",
                fontSize = when (windowSize.width) {
                    WindowType.Compact -> 16.sp
                    else -> 18.sp
                },
                fontWeight = FontWeight.SemiBold,
                color = Color.Blue
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Category filters - Adjusting the layout based on screen width
        when (windowSize.width) {
            WindowType.Compact -> {
                // Scrollable filters for small screens
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = true,
                        onClick = { /* Action */ },
                        label = { Text("All") },
                        modifier = Modifier.height(36.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color.Blue,
                            selectedLabelColor = Color.White
                        )
                    )

                    FilterChip(
                        selected = false,
                        onClick = { /* Action */ },
                        label = { Text("General") },
                        modifier = Modifier.height(36.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            labelColor = Color.Black
                        )
                    )

                    FilterChip(
                        selected = false,
                        onClick = { /* Action */ },
                        label = { Text("Dentist") },
                        modifier = Modifier.height(36.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            labelColor = Color.Black
                        )
                    )

                    FilterChip(
                        selected = false,
                        onClick = { /* Action */ },
                        label = { Text("Nutritionist") },
                        modifier = Modifier.height(36.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            labelColor = Color.Black
                        )
                    )
                }
            }
            else -> {
                // More spaced filters for larger screens
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FilterChip(
                        selected = true,
                        onClick = { /* Action */ },
                        label = { Text("All") },
                        modifier = Modifier.height(40.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color.Blue,
                            selectedLabelColor = Color.White
                        )
                    )

                    FilterChip(
                        selected = false,
                        onClick = { /* Action */ },
                        label = { Text("General") },
                        modifier = Modifier.height(40.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            labelColor = Color.Black
                        )
                    )

                    FilterChip(
                        selected = false,
                        onClick = { /* Action */ },
                        label = { Text("Dentist") },
                        modifier = Modifier.height(40.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            labelColor = Color.Black
                        )
                    )

                    FilterChip(
                        selected = false,
                        onClick = { /* Action */ },
                        label = { Text("Nutritionist") },
                        modifier = Modifier.height(40.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            labelColor = Color.Black
                        )
                    )

                    FilterChip(
                        selected = false,
                        onClick = { /* Action */ },
                        label = { Text("Ophthalmologist") },
                        modifier = Modifier.height(40.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            labelColor = Color.Black
                        )
                    )
                }
            }
        }
    }
}
