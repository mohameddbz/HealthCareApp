package com.example.projecttdm.ui.auth.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projecttdm.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    val scale = remember { Animatable(0f) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(true) {
        scale.animateTo(
            targetValue = 1.2f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = { OvershootInterpolator(4f).getInterpolation(it) }
            )
        )
        delay(2000)
        isLoading = false
        onFinish()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        val logoSize = this@BoxWithConstraints.maxWidth * 0.4f

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(logoSize)
                    .scale(scale.value)
            )

            Spacer(modifier = Modifier.height(this@BoxWithConstraints.maxHeight * 0.05f))

            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.outline,
                    strokeWidth = 4.dp
                )
            }
        }
    }
}
