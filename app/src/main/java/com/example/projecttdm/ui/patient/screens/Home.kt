package com.example.projecttdm.ui.patient.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.projecttdm.R
import com.example.projecttdm.data.model.Specialty
import com.example.projecttdm.state.UiState
import com.example.projecttdm.ui.common.components.DeconnectionButton
import com.example.projecttdm.ui.common.components.UserProfileImage
import com.example.projecttdm.ui.patient.PatientRoutes
import com.example.projecttdm.ui.patient.components.CategoryFilter
import com.example.projecttdm.ui.patient.components.CostumSearchBar
import com.example.projecttdm.ui.patient.components.DoctorSpecialitySection
import com.example.projecttdm.ui.patient.components.MedicalCheckBanner
import com.example.projecttdm.viewmodel.DoctorListViewModel
import com.example.projecttdm.viewmodel.DoctorSearchViewModel
import com.example.projecttdm.viewmodel.HomeViewModel


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(doctorSearchViewModel: DoctorSearchViewModel, doctorListViewModel: DoctorListViewModel = viewModel() , navController :NavHostController,homeViewModel: HomeViewModel) {

    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }

    val windowSize: WindowSize = calculateWindowSize()
    val doctors by doctorListViewModel.doctors.collectAsState()
    val selectedSpecialty by doctorSearchViewModel.selectedSpecialty.collectAsState()
    val specialties by doctorSearchViewModel.allSpecialties.collectAsState()

    val currentUser by homeViewModel.currentUser.collectAsState()

    Scaffold(
        topBar = {
            when (currentUser) {
                is UiState.Init -> {
                    // Ne rien afficher ou un écran vide
                }
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }
                is UiState.Success -> {
                    val user = (currentUser as UiState.Success).data
                    TopAppBar(
                        title = {
                            Column {
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
                                    text = user.first_name + " " + user.last_name ,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    maxLines = 1
                                )
                            }
                        },
                        navigationIcon = {
                            UserProfileImage(user.image)
                        },
                        actions = {
                            IconButton(onClick = { navController.navigate(PatientRoutes.NotificationScreen.route)}) {
                                Icon(
                                    imageVector = Icons.Outlined.Notifications,
                                    contentDescription = "Notifications",
                                    tint = Color.Black,
                                    modifier = Modifier.size(
                                        when (windowSize.width) {
                                            WindowType.Compact -> 24.dp
                                            else -> 28.dp
                                        }
                                    )
                                )
                            }
                            IconButton(onClick = { /* Favorite action */ }) {
                                Icon(
                                    imageVector = Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favorites",
                                    tint = Color.Black,
                                    modifier = Modifier.size(
                                        when (windowSize.width) {
                                            WindowType.Compact -> 24.dp
                                            else -> 28.dp
                                        }
                                    )
                                )
                            }
                        },
                        /* colors = TopAppBarDefaults.topAppBarColors(
                             containerColor = MaterialTheme.colorScheme.background
                         ) */
                    )

                }
                is UiState.Error -> {
                    val errorMessage = (currentUser as UiState.Error).message
                    Text("Erreur : $errorMessage", color = Color.Red)
                }
            }



        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {

            // Search Bar
            CostumSearchBar(
                windowSize = windowSize,
                query = searchQuery,
                onQueryChange =  setSearchQuery  ,
                onSearch = { query ->
                    // Action à effectuer lors de la recherche
                    println("Recherche: $query")
                },
                onFilterClick = {
                    println("Filter clicked")
                },
                doctorSearchViewModel = doctorSearchViewModel,
                navController = navController

            )

            Spacer(modifier = Modifier.height(16.dp))

            // Medical Check Banner
            MedicalCheckBanner(windowSize)

            Spacer(modifier = Modifier.height(16.dp))

            // Doctor Speciality Section
            DoctorSpecialitySection(windowSize)

            Spacer(modifier = Modifier.height(16.dp))

            // Top Doctors Section
            TopDoctorsSection(navController,windowSize,specialties,selectedSpecialty, {doctorSearchViewModel.setSpecialty(it)})

            Spacer(modifier = Modifier.height(8.dp)) // Space for bottom navigation
//            if (doctors.isEmpty()) {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("No doctors found.")
//                }
//            } else {
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .heightIn(300.dp, 600.dp)
//                        .padding(horizontal = 16.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    items(doctors) { doctor ->
//                        DoctorCard(
//                            doctor = doctor,
//                            isFavorite = false, // Could be managed via favorites list
//                            onFavoriteClick = { doctorListViewModel.toggleFavorite(doctor.id) },
//                            onDoctorClick = {  }
//                        )
//                    }
//                }
//            }
//            Spacer(modifier = Modifier.height(30.dp)) // Space for bottom navigation


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
fun TopDoctorsSection(navController : NavHostController, windowSize: WindowSize, specialtiess: List<Specialty>, selectedSpecialtyy: Specialty?, onSpecialtySelected: (Specialty) -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Top Doctors",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = when (windowSize.width) {
                    WindowType.Compact -> 18.sp
                    else -> 20.sp
                },
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier.clickable {
                    navController.navigate(PatientRoutes.topDoctors.route)
                },
                text = "See All",
                fontSize = when (windowSize.width) {
                    WindowType.Compact -> 16.sp
                    else -> 18.sp
                },
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        CategoryFilter(
            specialties = specialtiess, selectedSpecialty = selectedSpecialtyy, onSpecialtySelected = onSpecialtySelected,
        )
        DeconnectionButton()
    }
}
