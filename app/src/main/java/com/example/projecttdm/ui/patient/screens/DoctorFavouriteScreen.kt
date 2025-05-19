package com.example.projecttdm.ui.patient.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.data.model.FavoriteDoctorResponse
import com.example.projecttdm.ui.patient.components.DoctorCardFavourite
import com.example.projecttdm.ui.patient.components.RemoveFavoriteDialog
import com.example.projecttdm.viewmodel.FavoriteDoctorsViewModel
import com.example.projecttdm.viewmodel.FavoriteDoctorsViewModelFactory
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteDoctorsScreen(
    onBackClick: () -> Unit,
    viewModel: FavoriteDoctorsViewModel = viewModel(factory = FavoriteDoctorsViewModelFactory())
) {

    val favoriteDoctors by viewModel.favoriteDoctors.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState(null)

    var showRemoveDialog by remember { mutableStateOf(false) }
    var selectedDoctor by remember { mutableStateOf<FavoriteDoctorResponse?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("My Favorite Doctors") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refreshFavorites() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    error != null -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Error: $error",
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.refreshFavorites() }) {
                                Text("Retry")
                            }
                        }
                    }
                    favoriteDoctors.isEmpty() -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "You don't have any favorite doctors yet",
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(vertical = 16.dp)
                        ) {
                            items(favoriteDoctors) { favoriteDoctor ->
                                DoctorCardFavourite(
                                    doctor = favoriteDoctor,
                                    onDoctorClick = {
                                        selectedDoctor = favoriteDoctor
                                        showRemoveDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showRemoveDialog && selectedDoctor != null) {
        RemoveFavoriteDialog(
            doctor = selectedDoctor!!,
            onConfirm = {
                viewModel.toggleFavorite(selectedDoctor!!.doctor_id)
                showRemoveDialog = false
                selectedDoctor = null
            },
            onDismiss = {
                showRemoveDialog = false
                selectedDoctor = null
            }
        )
    }

    error?.let {
        LaunchedEffect(error) {
            delay(3000)
            viewModel.clearError()
        }
    }
}
