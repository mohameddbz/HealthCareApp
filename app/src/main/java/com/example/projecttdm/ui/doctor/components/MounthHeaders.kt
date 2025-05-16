package com.example.projecttdm.ui.doctor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projecttdm.ui.doctor.screens.DarkBlue
import com.example.projecttdm.ui.doctor.screens.LightBlue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun MonthHeader(
    currentDate: Date,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onPreviousMonth,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(LightBlue)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Mois précédent",
                tint = DarkBlue
            )
        }

        Text(
            text = dateFormat.format(currentDate).uppercase(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        IconButton(
            onClick = onNextMonth,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(LightBlue)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Mois suivant",
                tint = DarkBlue
            )
        }
    }
}

