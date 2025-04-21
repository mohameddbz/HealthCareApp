package com.example.projecttdm.ui.patient.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.projecttdm.R
import com.example.projecttdm.data.model.Doctor

@Composable
fun DoctorInfoCard(doctor: Doctor) {
    val lightGrayBackground = Color(0xFFF5F5F5)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(16.dp),
        color = lightGrayBackground
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Doctor info header
            Text(
                text = "Doctor Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Doctor profile
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Doctor image
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                ) {
                    if (doctor.imageResId != null) {
                        Image(
                            painter = painterResource(id = doctor.imageResId),
                            contentDescription = "Doctor",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    else {
                        Image(
                            painter = painterResource(id = R.drawable.doctor_image), // ajoute une image par défaut dans ton dossier res/drawable
                            contentDescription = "Default Doctor",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }


                }

                Spacer(modifier = Modifier.width(16.dp))

                // Doctor info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = doctor.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = doctor.hospital,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                // Doctor rating
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF2D9E)),

                )
            }
        }
    }
}