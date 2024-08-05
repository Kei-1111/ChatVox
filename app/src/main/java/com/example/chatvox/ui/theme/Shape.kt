package com.example.chatvox.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val shapes = Shapes(
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(topStart = 0.dp, topEnd = 50.dp, bottomStart = 50.dp, bottomEnd = 0.dp),
    extraLarge = RoundedCornerShape(24.dp),
)

object chatTextShape {
    val userTextShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 0.dp)
    val voxTextShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 0.dp, bottomEnd = 20.dp)
}