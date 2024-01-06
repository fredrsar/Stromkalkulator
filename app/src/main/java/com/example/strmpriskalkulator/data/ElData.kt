package com.example.strmpriskalkulator.data

/*
* Data object to handle the API call to get data regarding prices. We mainly use NOK_per_kWh.
* */
data class ElData(
    val NOK_per_kWh:Double,
    val EUR_per_kWh:Double,
    val EXR: Double,
    val time_start:String,
    val time_end:String
)
