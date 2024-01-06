package com.example.strmpriskalkulator.ui.components


import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp



//takes a list of pairs first value is date in string form, second is the price to create a graph over electricity prices
@Composable
fun StromprisGraf(stromdata: List<Pair<String, Float>>) {

    if (stromdata.isEmpty()) return

    var highestValue = stromdata[0].second
    stromdata.forEach {
        if (highestValue < it.second) {
            highestValue = it.second
        }
    }

    val fontSize: Float = if (stromdata.size < 15) {
        30f
    } else {
        0f
    }

    Canvas(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        val lineDistance = size.width / (stromdata.size - 1)

        stromdata.forEachIndexed { index, pair ->

            val paint = Paint()
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = fontSize
            paint.color = 0xFF42535C.toInt()

            drawIntoCanvas {
                it.nativeCanvas.drawText(pair.first, index * lineDistance, size.height, paint)
                it.nativeCanvas.drawText(
                    String.format("%.1f kr", pair.second),
                    index * lineDistance,
                    size.height + fontSize + 20,
                    paint
                )
            }


            drawCircle(
                color = Color.Black,
                radius = 10f,
                center = Offset(
                    x = index * lineDistance,
                    y = calculateYVal(highestValue = highestValue, pair.second, size.height)
                )
            )
            if (index == stromdata.size - 1) return@forEachIndexed
            drawLine(
                color = Color.Black,
                strokeWidth = 4f,
                start = Offset(
                    x = index * lineDistance,
                    y = calculateYVal(highestValue, pair.second, size.height)
                ),
                end = Offset(
                    x = (index + 1) * lineDistance,
                    y = calculateYVal(highestValue, stromdata[index + 1].second, size.height)
                )
            )

        }
    }
}

fun calculateYVal(
    highestValue: Float,
    currentValue: Float,
    canvasHeight: Float
): Float {
    val maxAndCurrentValueDiff = (highestValue - currentValue)

    val relativeScreen = (canvasHeight / highestValue)

    return maxAndCurrentValueDiff * relativeScreen
}

@Preview (showSystemUi = true)
@Composable
fun Preview() {
    StromprisGraf(
        listOf(
            Pair("1.april", 123.4f),
            Pair("2.april", 121.4f),
            Pair("3.april", 111.4f),
            Pair("4.april", 91.4f),
            Pair("5.april", 23.4f),
            Pair("6.april", 0.4f),
            Pair("1.april", 122.4f),
            Pair("1.april", 122.4f)
        )
    )
}