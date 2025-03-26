package com.example.projecttdm.ui.auth.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.R


@Composable
fun SignUpButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = colorResource(id = R.color.bleu)
        ),
        border = BorderStroke(2.dp, colorResource(id = R.color.bleu)),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(50.dp)
    ) {
        Text(text = "Sign Up", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}