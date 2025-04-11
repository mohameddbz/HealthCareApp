package com.example.projecttdm.ui.patient.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.ui.patient.WindowSize
import com.example.projecttdm.ui.patient.WindowType


@Composable
fun SearchBar(
    windowSize: WindowSize,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(when (windowSize.width) {
                WindowType.Compact -> 48.dp
                else -> 56.dp
            })
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray,
                modifier = Modifier.size(when (windowSize.width) {
                    WindowType.Compact -> 20.dp
                    else -> 24.dp
                })
            )

            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = when (windowSize.width) {
                        WindowType.Compact -> 14.sp
                        else -> 16.sp
                    }
                ),
                singleLine = true,
                cursorBrush = SolidColor(Color.Black),
                modifier = Modifier
                    .weight(1f)
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyUp) {
                            onSearch(query)
                            focusManager.clearFocus()
                            true
                        } else {
                            false
                        }
                    },
                decorationBox = { innerTextField ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                text = "Rechercher",
                                color = Color.Gray,
                                fontSize = when (windowSize.width) {
                                    WindowType.Compact -> 14.sp
                                    else -> 16.sp
                                }
                            )
                        }
                        innerTextField()
                    }
                }
            )

            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") },
                    modifier = Modifier.size(when (windowSize.width) {
                        WindowType.Compact -> 20.dp
                        else -> 24.dp
                    })
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Effacer",
                        tint = Color.Gray
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Filter",
                tint = Color.Blue,
                modifier = Modifier
                    .size(when (windowSize.width) {
                        WindowType.Compact -> 20.dp
                        else -> 24.dp
                    })
                    .clickable { /* Action pour le filtre */ }
            )
        }
    }
}

