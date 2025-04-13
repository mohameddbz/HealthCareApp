package com.example.projecttdm.ui.auth.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.navigation.NavHostController
import com.example.projecttdm.R

@Composable
fun OnboardingScreen(
    navController: NavHostController,
    nextPage: String,
    doctorImage: Int,
    titleText: String,
    indicatorColor1: Color,
    indicatorColor2: Color,
    indicatorColor3: Color
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Bouton Skip aligné à droite
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { navController.navigate("welcomScreen") }) {
                Text("Skip", color = MaterialTheme.colorScheme.outline)
            }
        }

        // Image du docteur
        Image(
            painter = painterResource(id = doctorImage),
            contentDescription = "Doctor",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(530.dp)
        )

        // Carte contenant le texte et le bouton
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(30.dp)
                )
                .padding(20.dp)
        ) {
            Column {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = titleText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(35.dp))

                // Indicateur de progression (trois points)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Indicateurs
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(indicatorColor1, indicatorColor2, indicatorColor3).forEachIndexed { index, color ->
                            Box(
                                modifier = Modifier
                                    .width(18.dp)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(color)
                            )
                            if (index < 2) Spacer(modifier = Modifier.width(4.dp))
                        }
                    }

                    // Bouton fléché
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(10.dp)
                            .clickable {
                                navController.navigate(nextPage)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Next",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }
    }
}
