package com.example.plantin.ui.Treatment

import androidx.compose.ui.graphics.Color

data class DiseaseItem(
    val title: String,
    val imageRes: Int,
    val backgroundColor: Color,
    val treatment: List<String>
)
