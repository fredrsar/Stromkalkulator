package com.example.strmpriskalkulator.ui.screens.testscreen

data class DetailedPriceUiState (
    val avgELPriceXDays: List<Pair<String, Float>>,
    val currentPrice: Double,
    val graphOptions: GraphOptions, // integer to show if the graph should show last 7 or 30 days
    val graphButtonActive: Boolean,
    val percentageLastXDays: Double // percentage of price compared to last (graphTimeInterval) days
)

enum class GraphOptions {
    Last7Days, Last30Days, Prediction7
}