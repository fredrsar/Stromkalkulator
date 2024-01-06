package com.example.strmpriskalkulator.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.strmpriskalkulator.R
import kotlin.math.roundToInt


@Composable
fun ProgressBarStrompris(text: String, progress: Float) {
    val list = stringArrayResource(id = R.array.colors_from_gree_to_red)

    val rightColor =
        if (progress < 0) {Color(list.first().toColorInt())}
        else Color(list[(progress * (list.size - 1)).roundToInt()]
            .toColorInt()
    )

    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = progress,
            strokeWidth = 15.dp, // Increase stroke width for a bolder look
            color = rightColor, // Change color to blue
            modifier = Modifier.size(180.dp)
        )
        Text(
            text = text,
            fontWeight = FontWeight.Bold, // Make text bold
            fontSize = 25.sp, // Increase font size
            textAlign = TextAlign.Center
        )
    }

}

/*
@Preview(showSystemUi = true)
@Composable
fun Preview() {
    ProgressBarStrompris(progress = 0.5f, text = "hei")
}
*/
