package com.example.strmpriskalkulator

import com.example.strmpriskalkulator.data.RepositoryInterface
import com.example.strmpriskalkulator.ui.screens.testscreen.GraphOptions
import com.example.strmpriskalkulator.viewmodel.StromprisViewModel
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import kotlinx.coroutines.test.setMain

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ViewModelTest {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            Dispatchers.setMain(Dispatchers.Unconfined) // Set the main dispatcher
        }
    }
    private val viewModel = StromprisViewModel(MockRepo())

    @Test
    fun loadSpotPriceComparison_isCorrect() {
        viewModel.loadSpotpriceComparison()
        assertEquals(
            1.234,
            viewModel.homeUiState.value.percentageComparison,
            0.00001
        )
    }

    @Test
    fun loadStromPrisData_isCorrect() {
        val homeUiState = viewModel.homeUiState.value
        val detailedPriceUiState = viewModel.detailedPriceUiState.value
        assertEquals(
            1.25 * 1.25,
            homeUiState.currentPrice,
            0.001
        )
        assertEquals(
            1.25 * 1.25,
            detailedPriceUiState.currentPrice,
            0.001
        )

        assertEquals(
            1.25 / 1.91,
            homeUiState.precentageOfHighestDayPrice,
            0.00001
        )
    }

    @Test
    fun loadForecast_isCorrect() {
        val homeUiState = viewModel.homeUiState.value

        assertEquals(
            15.0,
            homeUiState.currentTemp,
            0.1
        )

        assertEquals("cloudy", homeUiState.weatherSymbolCode)
    }

    @Test
    fun loadGraphData_isCorrect() {
        val detailedPriceUiState = viewModel.detailedPriceUiState.value

        assertEquals(
            4,
            detailedPriceUiState.avgELPriceXDays.size
        )

        detailedPriceUiState.avgELPriceXDays.forEach{
            assertEquals(
                (0.83591916666 * 1.25).toFloat(),
                it.second,
                0.001f
            )
        }
    }

    @Test
    fun changeTimeIntervalGraph_isCorrect() {
        viewModel.changeTimeIntervalGraph(GraphOptions.Last30Days)
        assertEquals(
            GraphOptions.Last30Days,
            viewModel.detailedPriceUiState.value.graphOptions
        )
    }

    @Test
    fun changePriceDistrict_isCorrect() {
        val priceDistrict = "hei"
        viewModel.changePriceDistrict(priceDistrict)

        assertEquals(
            priceDistrict,
            viewModel.priceDistrict
        )
    }



}


class MockRepo: RepositoryInterface {
    override suspend fun getCurrentSpotPrice(
        year: Int,
        month: Int,
        day: Int,
        currentHour: Int,
        priceDistrict: String
    ): Double {
        return 1.25
    }

    override suspend fun getSpotpriceComparison(
        year: Int,
        month: Int,
        day: Int,
        currentHour: Int,
        priceDistrict: String
    ): Double {
        return 1.234
    }

    override suspend fun getCurrentTemp(): Double {
        return 15.0
    }

    override suspend fun getCurrentSymbol(): String {
        return "cloudy"
    }

    override suspend fun getHighestDayPrice(
        year: Int,
        month: Int,
        day: Int,
        priceDistrict: String
    ): Double {
        return 1.91
    }

    override suspend fun getPriceFromLastXDays(
        year: Int,
        month: Int,
        day: Int,
        xDays: Int,
        priceDistrict: String
    ): List<Pair<String, Double>> {
        return listOf(
            Pair("19.05", 0.83591916666 * 1.25),
            Pair("20.05", 0.83591916666 * 1.25),
            Pair("21.05", 0.83591916666 * 1.25),
            Pair("22.05", 0.83591916666 * 1.25)
        )
    }

    override suspend fun getPriceForNextXDays(
        year: Int,
        month: Int,
        day: Int,
        xDays: Int,
        priceDistrict: String
    ): List<Pair<String, Double>> {
        return listOf()
    }

}