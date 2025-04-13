package com.example.projecttdm.ui.patient.components.Appointment


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MultilineTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val backgroundColor = Color(0xFFEEEEEE)

    Column {
        FieldLabel(text = label)

        StyledMultilineTextField(
            value = value,
            onValueChange = onValueChange,
            backgroundColor = backgroundColor
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
private fun StyledMultilineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    backgroundColor: Color
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 150.dp)
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
            fontSize = 14.sp,
            color = Color.Black,
            lineHeight = 20.sp
        ),
        maxLines = Int.MAX_VALUE,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        )
    )
}
