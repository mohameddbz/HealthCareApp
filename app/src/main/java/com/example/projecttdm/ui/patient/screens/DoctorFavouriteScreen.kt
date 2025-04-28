package com.example.projecttdm.ui.patient.screens



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.ui.patient.components.DoctorCard
import com.example.projecttdm.ui.patient.components.RemoveFavoriteDialog
import com.example.projecttdm.viewmodel.FavoriteDoctorsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteDoctorsScreen(
    onBackClick: () -> Unit,
    viewModel: FavoriteDoctorsViewModel = viewModel()
) {
    val favoriteDoctors by viewModel.favoriteDoctors.observeAsState(emptyList())


    var showRemoveDialog by remember { mutableStateOf(false) }
    var selectedDoctor by remember { mutableStateOf<Doctor?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // TopAppBar
            TopAppBar(
                title = { Text("My Favorite Doctor") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )



            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(favoriteDoctors) { doctor ->
                    DoctorCard(
                        doctor = doctor,
                        isFavorite = true, // Since this is the favorites screen
                        onFavoriteClick = {
                            // We don't need separate handling for favorite icon here
                            // as clicking anywhere on the card will show remove dialog
                        },
                        onDoctorClick = {
                            // Show dialog when clicking on doctor card
                            selectedDoctor = doctor
                            showRemoveDialog = true
                        }
                    )
                }
            }
        }
    }

    // Dialog for removing a doctor from favorites
    if (showRemoveDialog && selectedDoctor != null) {
        RemoveFavoriteDialog(
            doctor = selectedDoctor!!,
            onConfirm = {
                viewModel.toggleFavorite(selectedDoctor!!.id)
                showRemoveDialog = false
                selectedDoctor = null
            },
            onDismiss = {
                showRemoveDialog = false
                selectedDoctor = null
            }
        )
    }
}
