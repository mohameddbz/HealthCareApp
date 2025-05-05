package com.example.projecttdm.ui.patient.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.projecttdm.R
import com.example.projecttdm.ui.patient.screens.WindowSize
import com.example.projecttdm.ui.patient.screens.WindowType
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.projecttdm.data.model.Specialty
import com.example.projecttdm.state.UiState
import com.example.projecttdm.viewmodel.DoctorListViewModel
import com.example.projecttdm.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CostumSearchBar(
    windowSize: WindowSize,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onFilterClick: () -> Unit,
    doctorListViewModel: DoctorListViewModel,
    navController: NavHostController
) {
    OutlinedTextField(
        value = query,
        onValueChange = { newValue ->
            onQueryChange(newValue)
            doctorListViewModel.setSearchQuery(newValue)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        placeholder = { Text("Search doctor, drugs, articles...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier.size(
                    when (windowSize.width) {
                        WindowType.Compact -> 24.dp
                        else -> 26.dp
                    }
                )
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Filter",
                modifier = Modifier
                    .size(
                        when (windowSize.width) {
                            WindowType.Compact -> 24.dp
                            else -> 26.dp
                        }
                    )
                    .clickable {
                        onFilterClick()
//                        navController.navigate(PatientRoutes.filters.route)
                    }
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        singleLine = true
    )
}



data class DoctorSpeciality(
    val icon: Int,
    val title: String
)

val specialities = listOf(
    DoctorSpeciality(R.drawable.ophthalmology , "General"),
    DoctorSpeciality(R.drawable.dentistry  , "Dentist"),
    DoctorSpeciality(R.drawable.ophthalmology, "Ophthalmology"),
    DoctorSpeciality(R.drawable.nutrition, "Nutrition"),
    DoctorSpeciality(R.drawable.neurology, "Neurology"),
    DoctorSpeciality(R.drawable.pediatrics, "Pediatric"),
    DoctorSpeciality(R.drawable.radiology, "Radiology"),
    DoctorSpeciality(R.drawable.more, "More")
)

@Composable
fun DoctorSpecialitySection(windowSize: WindowSize,homeViewModel: HomeViewModel) {
    val context  = LocalContext.current;
    val specialtiesState by homeViewModel.specialtyUiState.collectAsState()
    Column {
        // Title Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Doctor Speciality",
                fontSize = if (windowSize.width == WindowType.Compact) 18.sp else 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "See All",
                fontSize = if (windowSize.width == WindowType.Compact) 16.sp else 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    Toast.makeText(context, "omba3d diro page specialité", Toast.LENGTH_SHORT).show()
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val columns = if (windowSize.width == WindowType.Compact) 4 else 8

        when (specialtiesState) {
            is UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Success -> {
                val specialties = (specialtiesState as UiState.Success<List<Specialty>>).data
                val displayedList = if (windowSize.width == WindowType.Compact) specialties.take(8) else specialties
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    userScrollEnabled = false
                ) {
                    items(displayedList) { speciality ->
                        SpecialityItem(
                            imageResId = homeViewModel.getIconForSpecialty(speciality.name),
                            title = speciality.name,
                            windowSize = windowSize
                        )
                    }
                }
            }

            is UiState.Error -> {
                Text(
                    text = (specialtiesState as UiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            UiState.Init -> {
                // Optional placeholder or do nothing
            }
        }
    }
}@Composable
fun SpecialityItem(imageResId: Int, title: String, windowSize: WindowSize) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp) // Taille spécifique
                .clip(CircleShape) // Rend le Box circulaire
                .background(MaterialTheme.colorScheme.primaryContainer), // Couleur de fond (facultatif)
            contentAlignment = Alignment.Center // Centre le contenu à l'intérieur du Box
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = title,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = if (windowSize.width == WindowType.Compact) 12.sp else 14.sp,
            maxLines = 1,
            fontWeight = FontWeight.SemiBold,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}



@Composable
fun MedicalCheckBanner(windowSize: WindowSize) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(
                when (windowSize.width) {
                    WindowType.Compact -> 130.dp
                    WindowType.Medium -> 160.dp
                    WindowType.Expanded -> 180.dp
                }
            )
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
                        .height(
                            when (windowSize.width) {
                                WindowType.Compact -> 36.dp
                                else -> 40.dp
                            }
                        )
                        .width(
                            when (windowSize.width) {
                                WindowType.Compact -> 120.dp
                                else -> 140.dp
                            }
                        ),
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


@Composable
fun UpcomingAppointmentBannner(
    windowSize: WindowSize,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Appointment details section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Doctor avatar placeholder
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

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Dr. Sarah Johnson",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Text(
                        text = "Cardiologist",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Appointment time and date
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppointmentInfo(
                            icon = Icons.Default.Schedule,
                            info = "02:30 PM"
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        AppointmentInfo(
                            icon = Icons.Default.DateRange,
                            info = "May 1, 2025"
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "View Details",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun AppointmentInfo(
    icon: ImageVector,
    info: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = info,
            fontSize = 14.sp,
            color = Color.DarkGray
        )
    }
}