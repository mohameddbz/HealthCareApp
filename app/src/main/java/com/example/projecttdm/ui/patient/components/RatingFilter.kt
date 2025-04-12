package com.example.projecttdm.ui.patient.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items

@Composable
fun RatingFilter(
    ratings: List<String>,
    selectedRating: String,
    onRatingSelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 10.dp)
    ) {
        items(ratings, key = { it }) { rating ->
            RatingChip(
                rating = rating,
                isSelected = selectedRating == rating,
                onClick = { onRatingSelected(rating) }
            )
        }
    }

}
