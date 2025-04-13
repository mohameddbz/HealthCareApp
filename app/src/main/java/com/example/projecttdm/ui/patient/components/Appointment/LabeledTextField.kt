package com.example.projecttdm.ui.patient.components.Appointment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true
) {
    val fieldBackgroundColor = Color(0xFFEEEEEE)

    Column {
        FieldLabel(text = label)

        StyledTextField(
            value = value,
            onValueChange = onValueChange,
            backgroundColor = fieldBackgroundColor,
            keyboardOptions = keyboardOptions,
            singleLine = singleLine
        )
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        ),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    backgroundColor: Color,
    keyboardOptions: KeyboardOptions,
    singleLine: Boolean
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            disabledContainerColor = backgroundColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        textStyle = LocalTextStyle.current.copy(
            fontSize = 16.sp,
            color = Color.Black
        ),
        singleLine = singleLine,
        keyboardOptions = keyboardOptions
    )
}
