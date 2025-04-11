package com.example.projecttdm.ui.doctor.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.ui.doctor.components.CategoryFilter
import com.example.projecttdm.ui.doctor.components.DoctorCard
import com.example.projecttdm.viewmodel.DoctorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopDoctorScreen(
    onBackClick: () -> Unit,
    onDoctorClick: (String) -> Unit,
    viewModel: DoctorViewModel = viewModel()
) {
    val doctors by viewModel.doctors.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Top Doctor",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Implement search */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                    IconButton(onClick = { /* TODO: Implement more options */ }) {
                        Text(
                            text = "⋯",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Category Filter
            CategoryFilter(
                categories = listOf("All", "General", "Dentist", "Nutritionist"),
                selectedCategory = selectedCategory,
                onCategorySelected = { viewModel.setCategory(it) }
            )

            // Doctor List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(doctors) { doctor ->
                    DoctorCard(
                        doctor = doctor,
                        isFavorite = false, // You'd track this in your ViewModel
                        onFavoriteClick = { viewModel.toggleFavorite(doctor.id) },
                        onDoctorClick = { onDoctorClick(doctor.id) }
                    )
                }
            }
        }
    }
}