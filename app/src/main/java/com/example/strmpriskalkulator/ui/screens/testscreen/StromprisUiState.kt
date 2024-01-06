package com.example.strmpriskalkulator.ui.screens.testscreen

import java.util.Date

data class StromprisUiState(
    val date: Date,
    val currentPrice: Double,
    val weatherSymbolCode : String,
    val currentTemp : Double,
    val percentageComparison : Double,
    val precentageOfHighestDayPrice: Double
)