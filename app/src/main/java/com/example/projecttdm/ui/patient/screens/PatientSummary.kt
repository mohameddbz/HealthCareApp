package com.example.projecttdm.ui.patient.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projecttdm.R
import kotlinx.coroutines.launch

@Composable

fun AppointmentReviewScreen(
    navController: NavController = rememberNavController(),
    onBackPressed: () -> Unit = {},
    onNextPressed: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val lightGrayBackground = Color(0xFFF5F5F5)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top bar with back button and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    scope.launch {
                        onBackPressed()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "Review Summary",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Doctor info in rounded container
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
                            Image(
                                painter = painterResource(id = R.drawable.dr),
                                contentDescription = "Doctor",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Doctor info
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Dr. Jenny Watson",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Immunologist",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Text(
                                text = "Christ Hospital in London, UK",
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
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "D",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }

            // Appointment details in rounded container
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
                    Text(
                        text = "Appointment Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    DetailRow("Date & Hour", "Dec 23, 2024 | 10:00 AM")
                    DetailRow("Package", "Messaging")
                    DetailRow("Duration", "30 minutes")
                }
            }

            // Patient details in rounded container
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
                    Text(
                        text = "Patient Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    DetailRow("Full Name", "John Smith")
                    DetailRow("Age", "42 years")
                    DetailRow("Sex", "Male")
                    DetailRow("Patient ID", "PT-7829456")
                    DetailRow("Blood Type", "O+")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom button
            Button(
                onClick = {
                    scope.launch {
                        onNextPressed()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(28.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3D7BF6)
                )
            ) {
                Text(
                    text = "Next",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppointmentReviewPreview() {
    val navController = rememberNavController()
    AppointmentReviewScreen(
        navController = navController,
        onBackPressed = {  },
        onNextPressed = { }
    )
}