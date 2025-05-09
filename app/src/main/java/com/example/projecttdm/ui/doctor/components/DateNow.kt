package com.example.projecttdm.ui.doctor.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import com.example.projecttdm.ui.doctor.screens.PrimaryColor
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun DateText() {
    val currentDate = remember {
        val locale = java.util.Locale("fr", "FR")
        val dateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", locale)
        dateFormat.format(Date())
    }

    Text(
        text = currentDate,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Medium,
        color = PrimaryColor
    )
}