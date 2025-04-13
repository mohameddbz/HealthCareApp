package com.example.projecttdm.ui.patient.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter

import com.example.projecttdm.data.model.Doctor

@Composable
fun DoctorCard(
    doctor: Doctor,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit,
    onDoctorClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onDoctorClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Doctor Image (Rectangle)
            Box(
                modifier = Modifier
                    .height(80.dp) // Set fixed height for rectangle shape
                    .width(80.dp) // Set fixed width
                    .clip(RoundedCornerShape(8.dp)) // Rounded corners for the rectangle
                    .background(Color(0xFFE0E0E0)) // Background color
            ) {
                if (doctor.imageResId != null) {
                    Image(
                        painter = painterResource(id = doctor.imageResId),
                        contentDescription = "Doctor ${doctor.name}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (!doctor.imageUrl.isNullOrBlank()) {
                    Image(
                        painter = rememberImagePainter(
                            data = doctor.imageUrl,
                            builder = {
                                crossfade(true)
//                                placeholder(R.drawable.doctor_placeholder)
//                                error(R.drawable.doctor_placeholder)
                            }
                        ),
                        contentDescription = "Doctor ${doctor.name}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Doctor Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dr. ${doctor.name}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    // Favorite Icon on the Right
                    Spacer(modifier = Modifier.weight(1f)) // This pushes the icon to the right
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color(0xFFE91E63) else Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${doctor.specialty.name} | ${doctor.hospital}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = " ${doctor.rating} (${doctor.reviewCount} reviews)",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}