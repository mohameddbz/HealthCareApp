package com.example.projecttdm.ui.auth.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignUpButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(50.dp)
    ) {
        Text(
            text = "Sign Up",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
