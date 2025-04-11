package com.example.projecttdm.ui.patient

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.R
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
fun MedicalAppScreen() {
    var searchQuery by remember { mutableStateOf("") }

    val windowSize: WindowSize = calculateWindowSize()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = when (windowSize.width) {
                WindowType.Compact -> 16.dp
                WindowType.Medium -> 24.dp
                WindowType.Expanded -> 32.dp
            })
            .verticalScroll(rememberScrollState())
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

        // Bottom Navigation Bar will be positioned at the bottom of the screen
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
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Image
        Image(
            painter = painterResource(id = R.drawable.doctor_image2),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(when (windowSize.width) {
                    WindowType.Compact -> 40.dp
                    else -> 48.dp
                })
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
                fontWeight = FontWeight.Bold
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
fun MedicalCheckBanner(windowSize: WindowSize) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(when (windowSize.width) {
                WindowType.Compact -> 130.dp
                WindowType.Medium -> 160.dp
                WindowType.Expanded -> 180.dp
            })
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF4B89FF))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Medical Checks!",
                    color = Color.White,
                    fontSize = when (windowSize.width) {
                        WindowType.Compact -> 20.sp
                        WindowType.Medium -> 22.sp
                        WindowType.Expanded -> 24.sp
                    },
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Check your health condition regularly to minimize the incidence of disease in the future.",
                    color = Color.White,
                    fontSize = when (windowSize.width) {
                        WindowType.Compact -> 12.sp
                        else -> 14.sp
                    },
                    maxLines = when (windowSize.width) {
                        WindowType.Compact -> 3
                        else -> 4
                    },
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { /* Action */ },
                    modifier = Modifier
                        .height(when (windowSize.width) {
                            WindowType.Compact -> 36.dp
                            else -> 40.dp
                        })
                        .width(when (windowSize.width) {
                            WindowType.Compact -> 120.dp
                            else -> 140.dp
                        }),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(
                        text = "Check Now",
                        color = Color(0xFF4B89FF),
                        fontSize = when (windowSize.width) {
                            WindowType.Compact -> 12.sp
                            else -> 14.sp
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Doctor Image
            Image(
                painter = painterResource(id = R.drawable.doctor_image2),
                contentDescription = "Doctor",
                modifier = Modifier
                    .size(when (windowSize.width) {
                        WindowType.Compact -> 100.dp
                        WindowType.Medium -> 120.dp
                        WindowType.Expanded -> 140.dp
                    }),
                contentScale = ContentScale.Fit
            )
        }

        // Pagination Dots
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 0..2) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(if (i == 0) Color.White else Color.White.copy(alpha = 0.5f))
                        .padding(horizontal = 2.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}
data class DoctorSpeciality(
    val icon: Int,
    val title: String
)

val specialities = listOf(
    DoctorSpeciality(R.drawable.ophthalmology , "General"),
    DoctorSpeciality(R.drawable.dentistry  , "Dentist"),
    DoctorSpeciality(R.drawable.ophthalmology, "Ophthalmology"),
    /*  DoctorSpeciality(Icons.Default.Person, "Nutrition"),
      DoctorSpeciality(Icons.Default.Person, "Neurology"),
      DoctorSpeciality(Icons.Default.Person, "Pediatric"),
      DoctorSpeciality(Icons.Default.Person, "Radiology"),
      DoctorSpeciality(Icons.Default.MoreVert, "More") */
)

@Composable
fun DoctorSpecialitySection(windowSize: WindowSize) {
    Column {
        // Title Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Doctor Speciality",
                fontSize = if (windowSize.width == WindowType.Compact) 16.sp else 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "See All",
                fontSize = if (windowSize.width == WindowType.Compact) 14.sp else 16.sp,
                color = Color.Blue
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid Section
        val columns = if (windowSize.width == WindowType.Compact) 3 else 6
        val displayedList = if (windowSize.width == WindowType.Compact) specialities.take(6) else specialities

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp), // adjust as needed
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false // optional: disable scrolling inside this section
        ) {
            items(displayedList) { speciality ->
                SpecialityItem(
                    imageResId = speciality.icon,
                    title = speciality.title,
                    windowSize = windowSize
                )
            }
        }
    }
}@Composable
fun SpecialityItem(imageResId: Int, title: String, windowSize: WindowSize) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = title,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = if (windowSize.width == WindowType.Compact) 12.sp else 14.sp
        )
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
                fontSize = when (windowSize.width) {
                    WindowType.Compact -> 16.sp
                    else -> 18.sp
                },
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "See All",
                fontSize = when (windowSize.width) {
                    WindowType.Compact -> 14.sp
                    else -> 16.sp
                },
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
