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
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        val screenHeight = maxHeight
        val imageHeight = screenHeight * 0.6f
        val cardPadding = screenHeight * 0.025f
        val cardCornerRadius = screenHeight * 0.04f

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            // Skip Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { navController.navigate("welcomScreen")}) {
                    Text("Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Doctor Image
            Image(
                painter = painterResource(id = doctorImage),
                contentDescription = "Doctor",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
            )

            // Bottom Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(cardCornerRadius)
                    )
                    .padding(cardPadding)
            ) {
                Column {
                    Spacer(modifier = Modifier.height(screenHeight * 0.02f))

                    Text(
                        text = titleText,
                        fontSize = (screenHeight.value * 0.03).sp, // ~24.sp on 800dp screen
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(screenHeight * 0.04f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Indicators
                        Row(verticalAlignment = Alignment.CenterVertically) {
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

                        // Arrow Button
                        Box(
                            modifier = Modifier
                                .size(screenHeight * 0.065f) // ~50.dp
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .clickable { navController.navigate(nextPage) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Next",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(screenHeight * 0.04f) // ~30.dp
                            )
                        }
                    }
                }
            }
        }
    }
}
