package com.example.strmpriskalkulator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun RoundButton(
    icon: Painter,
    size: Int,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val iconTint = if (isSelected) colors.primary else colors.secondary

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .then(Modifier.size(size.dp))
                .background(colors.surface, CircleShape)
                .border(1.dp, colors.primaryVariant, shape = CircleShape)
        ) {
            Icon(icon, contentDescription = "content description", tint = iconTint)
        }

        Spacer(modifier = Modifier.height(7.dp))

        Text(text, color = colors.onPrimary)
    }
}