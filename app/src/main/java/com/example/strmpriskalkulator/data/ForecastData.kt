package com.example.strmpriskalkulator.data


/* Data object for Forecast data. Here we create data classes for all the different objects in the JSON from the API.
We don't use much of the data, but have objects for all to have available.
* */
class ForecastData (
    val type: String,
    val geometry: Geometry,
    val properties: Properties
)

data class Geometry(
    val type: String,
    val coordinates: List<Double>
)

data class Properties(
    val meta: Meta,
    val timeseries: List<TimeSeries>
)

data class Meta(
    val updated_at: String,
    val units: Map<String, String> // kan ignorere
)

data class TimeSeries(
    val time: String,
    val data: Data
)

data class Data(
    val instant: Instant,
    val next_12_hours: Next12Hours,
    val next_1_hours: Next1Hours,
    val next_6_hours: Next6Hours
)

data class Instant(
    val details: Details
)

data class Details(
    val air_pressure_at_sea_level: Double,
    val air_temperature: Double, // Dette er også noe vi vil ha
    val cloud_area_fraction: Double,
    val relative_humidity: Double,
    val wind_from_direction: Double,
    val wind_speed: Double,
)
data class Next12Hours(
    val summary: Summary
)

data class Summary(
    val symbol_code: String // Dette er interessant
)

data class Next1Hours(
    val summary: Summary,
    val details: DetailsNext1Hours
)

data class DetailsNext1Hours(
    val precipitation_amount: Double,
)

data class Next6Hours(
    val summary: Summary,
    val details: DetailsNext6Hours
)

data class DetailsNext6Hours(
    val air_temperature_max: Double,
)
