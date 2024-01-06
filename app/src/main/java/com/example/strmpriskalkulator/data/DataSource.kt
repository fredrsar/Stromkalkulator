package com.example.strmpriskalkulator.data

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*


interface DataSourceInterface {
    suspend fun fetchHvaKosterStromen(extension: String): List<ElData>
    suspend fun fetchForecastData(): ForecastData
}
/* Datasource object with baseURL as a parameter. Given what API we want information from we call either function.
* For LocationForecast we have an apiKey to connect. */
class DataSource (private val baseUrl: String) : DataSourceInterface{
    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }
    override suspend fun fetchHvaKosterStromen(extension: String): List<ElData> {
        Log.d("hei", "$baseUrl$extension")
        return client.get("$baseUrl$extension").body()
    }

    private val apiKey= "bbb0d95f-af86-4779-bf64-e5616cd16cdb"
    override suspend fun fetchForecastData(): ForecastData {
        val response = client.get(baseUrl) {
            headers {
                append("X-Gravitee-API-Key", apiKey)
            }
        }
        return response.body()
    }

}