package com.example.projecttdm.ui.patient.components.DoctorProfile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.ui.patient.components.DoctorCard
import com.example.projecttdm.viewmodel.FavoriteDoctorsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoctorList(
    doctors: List<Doctor>,
    onDoctorClick: (String) -> Unit,
    favoriteViewModel: FavoriteDoctorsViewModel
) {
    val favoriteDoctors by favoriteViewModel.favoriteDoctors.observeAsState(emptyList())
    val isLoading by favoriteViewModel.isLoading.observeAsState(false)
    val error by favoriteViewModel.error.observeAsState()

    // Handle error state
    error?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            // You can show a snackbar or toast here
            // For now, just clear the error after showing it
            favoriteViewModel.clearError()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(doctors, key = { it.id }) { doctor ->
            val isFavorite = favoriteDoctors.any { it.doctor_id == doctor.id.toInt() }

            DoctorCard(
                doctor = doctor,
                isFavorite = isFavorite,
                onFavoriteClick = {
                    favoriteViewModel.toggleFavorite(doctor.id.toInt())
                },
                onDoctorClick = { onDoctorClick(doctor.id) }
            )
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}