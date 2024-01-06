package com.example.strmpriskalkulator

import com.example.strmpriskalkulator.data.*
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlinx.coroutines.runBlocking


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class RepositoryTest {

    private val dataSource = MockDataSource()
    private val repository = Repository(stromprisDataSource = dataSource, forecastDataSource = dataSource)
    private val mockRepo = MockRepo(stromprisDataSource = dataSource, forecastDataSource = dataSource)

    @Test
    fun getHighestDayPrice_isCorrect() {
        runBlocking {
            assertEquals(
                repository.getHighestDayPrice(0, 0 , 0 , ""),
                1.03179,
                0.001
            )
        }
    }

    @Test
    fun getCurrentSpotPrice_isCorrect() {

        var spotPrice: Double
        runBlocking {
            spotPrice = repository.getCurrentSpotPrice(2023, 5, 22, 1,"NO1")
        }
        assertEquals(
            spotPrice,
            0.81111,
            0.00001
        )

        runBlocking {
            spotPrice = repository.getCurrentSpotPrice(2023, 5, 22, 12,"NO1")
        }
        assertEquals(
            spotPrice,
            0.75998,
            0.0001
        )
    }

    @Test
    fun getPriceForLastXDays_isCorrect() {
        val lastXDays: List<Pair<String, Double>>
        runBlocking {
            // 4 is may, begins at 0
            lastXDays = repository.getPriceFromLastXDays(2023,4,22, 4, "")
        }
        val expectedResult = listOf(
            Pair("19.05", 0.83591916666 * 1.25),
            Pair("20.05", 0.83591916666 * 1.25),
            Pair("21.05", 0.83591916666 * 1.25),
            Pair("22.05", 0.83591916666 * 1.25)
        )
        lastXDays.forEachIndexed { index, pair ->
            assertEquals(
                pair.first , expectedResult[index].first
            )
            assertEquals(
                expectedResult[index].second,
                pair.second,
                0.0001
            )
        }
    }

    @Test
    fun getCurrentSymbol_isCorrect() {
        runBlocking {
            val symbol = mockRepo.getCurrentSymbol()
            assertEquals("cloudy", symbol)
        }
    }

    @Test
    fun getSpotpriceComparison_isCorrect() {
        runBlocking {
            val percentageChange = repository.getSpotpriceComparison(0,0,0,1,"")
            assertEquals(
                ((0.81111 - 0.83591916666) / 0.83591916666) * 100,
                percentageChange,
                0.001
            )
        }
    }

    class MockRepo(
        stromprisDataSource: DataSourceInterface,
        forecastDataSource: DataSourceInterface
    ) : Repository(stromprisDataSource = stromprisDataSource, forecastDataSource = forecastDataSource) {
        override fun getZTime(): String {
            return "2023-05-23T09:00:00Z"
        }
    }
}


class MockDataSource : DataSourceInterface {
    override suspend fun fetchHvaKosterStromen(extension: String): List<ElData> {
        return listOf(
            ElData(NOK_per_kWh=0.82866, EUR_per_kWh=0.07082, EXR=11.701, time_start="2023-05-22T00:00:00+02:00", time_end="2023-05-22T01:00:00+02:00"),
            ElData(NOK_per_kWh=0.81111, EUR_per_kWh=0.06932, EXR=11.701, time_start="2023-05-22T01:00:00+02:00", time_end="2023-05-22T02:00:00+02:00"),
            ElData(NOK_per_kWh=0.80819, EUR_per_kWh=0.06907, EXR=11.701, time_start="2023-05-22T02:00:00+02:00", "time_end=2023-05-22T03:00:00+02:00"),
            ElData(NOK_per_kWh=0.80842, EUR_per_kWh=0.06909, EXR=11.701, time_start="2023-05-22T03:00:00+02:00", time_end="2023-05-22T04:00:00+02:00"),
            ElData(NOK_per_kWh=0.81041, EUR_per_kWh=0.06926, EXR=11.701, time_start="2023-05-22T04:00:00+02:00", time_end="2023-05-22T05:00:00+02:00"),
            ElData(NOK_per_kWh=0.8241, EUR_per_kWh=0.07043, EXR=11.701, time_start="2023-05-22T05:00:00+02:00", time_end="2023-05-22T06:00:00+02:00"),
            ElData(NOK_per_kWh=0.80971, EUR_per_kWh=0.0692, EXR=11.701, time_start="2023-05-22T06:00:00+02:00", time_end="2023-05-22T07:00:00+02:00"),
            ElData(NOK_per_kWh=0.90004, EUR_per_kWh=0.07692, EXR=11.701, time_start="2023-05-22T07:00:00+02:00", time_end="2023-05-22T08:00:00+02:00"),
            ElData(NOK_per_kWh=1.03179, EUR_per_kWh=0.08818, EXR=11.701, time_start="2023-05-22T08:00:00+02:00", time_end="2023-05-22T09:00:00+02:00"),
            ElData(NOK_per_kWh=0.9424, EUR_per_kWh=0.08054, EXR=11.701, time_start="2023-05-22T09:00:00+02:00", time_end="2023-05-22T10:00:00+02:00"),
            ElData(NOK_per_kWh=0.91209, EUR_per_kWh=0.07795, EXR=11.701, time_start="2023-05-22T10:00:00+02:00", time_end="2023-05-22T11:00:00+02:00"),
            ElData(NOK_per_kWh=0.83639, EUR_per_kWh=0.07148, EXR=11.701, time_start="2023-05-22T11:00:00+02:00", time_end="2023-05-22T12:00:00+02:00"),
            ElData(NOK_per_kWh=0.75998, EUR_per_kWh=0.06495, EXR=11.701, time_start="2023-05-22T12:00:00+02:00", time_end="2023-05-22T13:00:00+02:00"),
            ElData(NOK_per_kWh=0.73564, EUR_per_kWh=0.06287, EXR=11.701, time_start="2023-05-22T13:00:00+02:00", time_end="2023-05-22T14:00:00+02:00"),
            ElData(NOK_per_kWh=0.77121, EUR_per_kWh=0.06591, EXR=11.701, time_start="2023-05-22T14:00:00+02:00", time_end="2023-05-22T15:00:00+02:00"),
            ElData(NOK_per_kWh=0.78397, EUR_per_kWh=0.067, EXR=11.701, time_start="2023-05-22T15:00:00+02:00", time_end="2023-05-22T16:00:00+02:00"),
            ElData(NOK_per_kWh=0.79099, EUR_per_kWh=0.0676, EXR=11.701, time_start="2023-05-22T16:00:00+02:00", time_end="2023-05-22T17:00:00+02:00"),
            ElData(NOK_per_kWh=0.82539, EUR_per_kWh=0.07054, EXR=11.701, time_start="2023-05-22T17:00:00+02:00", time_end="2023-05-22T18:00:00+02:00"),
            ElData(NOK_per_kWh=0.83475, EUR_per_kWh=0.07134, EXR=11.701, time_start="2023-05-22T18:00:00+02:00", time_end="2023-05-22T19:00:00+02:00"),
            ElData(NOK_per_kWh=0.87383, EUR_per_kWh=0.07468, EXR=11.701, time_start="2023-05-22T19:00:00+02:00", time_end="2023-05-22T20:00:00+02:00"),
            ElData(NOK_per_kWh=0.86084, EUR_per_kWh=0.07357, EXR=11.701, time_start="2023-05-22T20:00:00+02:00", time_end="2023-05-22T21:00:00+02:00"),
            ElData(NOK_per_kWh=0.84821, EUR_per_kWh=0.07249, EXR=11.701, time_start="2023-05-22T21:00:00+02:00", time_end="2023-05-22T22:00:00+02:00"),
            ElData(NOK_per_kWh=0.83159, EUR_per_kWh=0.07107, EXR=11.701, time_start="2023-05-22T22:00:00+02:00", time_end="2023-05-22T23:00:00+02:00"),
            ElData(NOK_per_kWh=0.82235, EUR_per_kWh=0.07028, EXR=11.701, time_start="2023-05-22T23:00:00+02:00", time_end="2023-05-23T00:00:00+02:00")
        )
    }

    override suspend fun fetchForecastData(): ForecastData {
        // hei
        return ForecastData(
            "", geometry = Geometry("", listOf()),
            Properties(Meta("",mapOf()),
                timeseries = listOf(
                   TimeSeries(
                       time = "2023-05-23T09:00:00Z",
                       data = Data(
                           Instant(
                               Details(
                                   air_pressure_at_sea_level = 0.0,
                                   air_temperature = 19.1,
                                   cloud_area_fraction = 0.0,
                                   relative_humidity = 0.0,
                                   wind_from_direction = 0.0,
                                   wind_speed = 0.0
                               )
                           ),
                           next_1_hours = Next1Hours(
                               summary = Summary(symbol_code = "cloudy"),
                               details = DetailsNext1Hours(precipitation_amount = 12.0)
                           ),
                           next_6_hours = Next6Hours(
                               summary = Summary(symbol_code = "rain"),
                               details = DetailsNext6Hours(air_temperature_max = 18.0)
                           ),
                           next_12_hours = Next12Hours(
                                summary = Summary(symbol_code = "rain")
                           )
                       )
                   )
                )
            )
        )
    }



}
