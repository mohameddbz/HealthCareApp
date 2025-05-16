package com.example.projecttdm.ui.doctor.screens



import androidx.compose.animation.AnimatedVisibility
import com.example.projecttdm.ui.doctor.components.AppointmentItem
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.projecttdm.R
import com.example.projecttdm.doctorviewmodel.DoctorHomeViewModel
import com.example.projecttdm.state.UiState
import com.example.projecttdm.ui.common.components.DeconnectionButton
import com.example.projecttdm.ui.common.components.UserProfileImage
import com.example.projecttdm.ui.doctor.DoctorRoutes
import com.example.projecttdm.ui.doctor.components.DateText
import com.example.projecttdm.ui.doctor.components.NextAppointmentCard
import com.example.projecttdm.ui.doctor.components.StatCard
import com.example.projecttdm.ui.patient.PatientRoutes
import com.example.projecttdm.ui.patient.screens.WindowType
import kotlinx.coroutines.launch

// Palette de couleurs moderne et professionnelle
val PrimaryColor = Color(0xFF4285F4)  // Bleu Google
val SecondaryColor = Color(0xFF34A853)  // Vert Google
val LightBackgroundColor = Color(0xFFF8F9FF)
val CardBackgroundColor = Color.White
val GrayTextColor = Color(0xFF5F6368)
val HighlightColor = Color(0xFFEA4335)  // Rouge Google

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorHomeScreen(doctorHomeViewModel : DoctorHomeViewModel = viewModel(),navController: NavHostController) {
    val currentUser by doctorHomeViewModel.currentUser.collectAsState()

    val nextAppointment by doctorHomeViewModel.nextAppointment.collectAsState()

    val todayAppointment by doctorHomeViewModel.toDaysAppointment.collectAsState()

    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var selectedTabIndex by remember { mutableStateOf(0) }
    val appointmentSamples = remember {
        listOf(
            AppointmentData("Sarah Johnson", "10:30 AM", "Consultation", R.drawable.doctor_image3),
            AppointmentData("Michael Chen", "11:45 AM", "Follow-up", R.drawable.doctor_image),
            AppointmentData("Emma Rodriguez", "2:15 PM", "Test Results", R.drawable.doctor_image3),
            AppointmentData("James Wilson", "3:30 PM", "New Patient", R.drawable.doctor_image2),
            AppointmentData("Olivia Martinez", "4:45 PM", "Therapy Session", R.drawable.doctor_image3)
        )
    }

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
                                        fontSize = 16.sp,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "👋",
                                        fontSize = 16.sp,
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
                            IconButton(onClick = {
                            /* navController.navigate(PatientRoutes.NotificationScreen.route) */
                                doctorHomeViewModel.getTodaysAppointment()
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Notifications,
                                    contentDescription = "Notifications",
                                    tint = Color.Black,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        },

                    )

                }
                is UiState.Error -> {
                    val errorMessage = (currentUser as UiState.Error).message
                    Text("Erreur : $errorMessage", color = Color.Red)
                }
            }
        },

        containerColor = LightBackgroundColor
    ) { paddingValues ->
        LazyColumn(
            state = lazyListState,
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item{
                DeconnectionButton()
            }

            item{
            Box(
                modifier = Modifier.padding(vertical=6.dp)
            ) {
                DateText()
            }
        }
            // Prochain rendez-vous
            item {
                Spacer(modifier = Modifier.height(16.dp))
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically()
                ) {
                    when(nextAppointment){
                        is UiState.Init -> {
                            // Ne rien afficher ou un écran vide
                        }
                        is UiState.Loading -> {
                            CircularProgressIndicator()
                        }
                        is UiState.Success -> {
                            val nextAppointment = (nextAppointment as UiState.Success).data
                            NextAppointmentCard(nextAppointment.nextAppointment)

                        }
                        is UiState.Error -> {
                            val errorMessage = (nextAppointment as UiState.Error).message
                            Text("Erreur : $errorMessage", color = Color.Red)
                        }

                    }
                }
            }

            // Stats rapides
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Vos statistiques",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Aujourd'hui",
                        value = "8",
                        icon = Icons.Rounded.Today,
                        iconTint = PrimaryColor,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "En attente",
                        value = "3",
                        icon = Icons.Rounded.AccessTime,
                        iconTint = HighlightColor,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Évaluation",
                        value = "4.9",
                        icon = Icons.Rounded.Star,
                        iconTint = SecondaryColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Section Rendez-vous
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Today's Appointment",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )

                    TextButton(onClick = { /* Voir tous */ }) {
                        Text(
                            "See All",
                            color = PrimaryColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

          item{
              when (todayAppointment) {
                  is UiState.Init -> {
                      // Ne rien afficher ou un écran vide
                  }
                  is UiState.Loading -> {
                      CircularProgressIndicator()
                  }
                  is UiState.Success -> {
                      val appointmentDays = (todayAppointment as UiState.Success).data
                      Text(
                          "HELLO WORLD WE ARE FINE "
                      )
                      // Liste des rendez-vous
                      /*     items(appointmentSamples) { appointment ->
                               AppointmentItem(
                                   name = appointment.name,
                                   time = appointment.time,
                                   type = appointment.type,
                                   imageRes = appointment.imageRes
                               )
                           } */
                  }
                  is UiState.Error -> {
                    /*  val errorMessage = (todayAppointment as UiState.Error).message
                      Text("Erreur : $errorMessage", color = Color.Red) */
                      val message = (todayAppointment as UiState.Error).message
                      // Afficher un message d'erreur
                      Text(text = message)
                  }

              }

          }


            // Espace en bas pour éviter que le dernier élément soit caché par la barre de navigation
            item {
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}

// Classes de données pour la démonstration
data class NavigationItem(val title: String, val icon: ImageVector)
data class AppointmentData(val name: String, val time: String, val type: String, val imageRes: Int)


