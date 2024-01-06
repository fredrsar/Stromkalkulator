package com.example.strmpriskalkulator.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.strmpriskalkulator.data.RepositoryInterface
import com.example.strmpriskalkulator.ui.screens.testscreen.DetailedPriceUiState
import com.example.strmpriskalkulator.ui.screens.testscreen.GraphOptions
import com.example.strmpriskalkulator.ui.screens.testscreen.PricePerActivityUiState
import com.example.strmpriskalkulator.ui.screens.testscreen.StromprisUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*


class StromprisViewModel(val repository: RepositoryInterface): ViewModel(){
    private var _homeUiState = MutableStateFlow(
        StromprisUiState( date = Calendar.getInstance().time, currentPrice = 1.5 , weatherSymbolCode = "fair_day", currentTemp = 7.5, percentageComparison = 0.0, precentageOfHighestDayPrice = 0.0)
    )
    val homeUiState: StateFlow<StromprisUiState> = _homeUiState.asStateFlow()

    private var _detailedPriceUiState = MutableStateFlow(
        DetailedPriceUiState(
            avgELPriceXDays = listOf(
                Pair("1.4", 1.23f),
                Pair("2.4", 1.00f),
                Pair("3.4", 0.76f),
                Pair("4.4", 1.12f),
                Pair("5.4", 1.22f),
                Pair("6.4", 1.15f),
                Pair("7.4", 1.22f)
            ),
            currentPrice = 1.5,
            graphOptions = GraphOptions.Last7Days,
            graphButtonActive = true,
            percentageLastXDays = 0.0
        )
    )
    val detailedPriceUiState: StateFlow<DetailedPriceUiState> = _detailedPriceUiState.asStateFlow()

    private var _pricePerActivityUiState = MutableStateFlow(
        PricePerActivityUiState(spotPrice = 1.5)
    )
    val pricePerActivityUiState: StateFlow<PricePerActivityUiState> = _pricePerActivityUiState.asStateFlow()


    var priceDistrict = "NO1"

    private var currentHour = -1 // base value
    private var currentDay = -1 // base value
    init {
        viewModelScope.launch {
            while(true) { //Always doing this<3
                //This will every minute check if the hour or day has changed, and if it has then it will call necessary functions.
                // will be "changed" first time because of base values = -1
                checkHourChanged()
                checkDayChanged()
                delay(60*1000L) //Che
            }
        }
    }

    // updates graph data when the day changes
    private fun checkDayChanged() {
        val now = Calendar.getInstance().time
        val calendar = Calendar.getInstance()
        calendar.time = now
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        if (day != currentDay) {
            currentDay = day // Then the hour gets updated and the functions to update values in app gets called
            loadGraphData()
        }
    }

    private fun checkHourChanged() { // this gets called every minute. This makes sure the app gets updated correctly
        val now = Calendar.getInstance().time
        val calendar = Calendar.getInstance()
        calendar.time = now
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        if (hour != currentHour) {
            currentHour = hour // Then the hour gets updated and the functions to update values in app gets called
            loadStromprisData()
            loadForecast()
            loadTime()
            loadSpotpriceComparison()

        }
    }
    private fun loadTime() { // this is to also change the date value in UiState
        viewModelScope.launch {
            _homeUiState.update { currentState ->
                currentState.copy(date = Calendar.getInstance().time) // update this
            }
        }
    }

    /* This is to load the spotprice comparison on the screen using functions in repository given the current data and time.
    * */
    fun loadSpotpriceComparison() {
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // January is 0
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        viewModelScope.launch {
            _homeUiState.update{ currentState ->
                currentState.copy(
                    percentageComparison = repository.getSpotpriceComparison(
                        year = year,
                        month = month,
                        day = day,
                        currentHour = hour,
                        priceDistrict = priceDistrict
                    )
                )
            }
        }
    }

    // Loads the prices and updates the current spot-price as well as the highest price today
    private fun loadStromprisData() {
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // January is 0
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)


        viewModelScope.launch {
            val currentSpotPrice: Double = repository.getCurrentSpotPrice(
                year = year,
                month = month,
                day = day,
                currentHour = hour,
                priceDistrict = priceDistrict
            )
            val highestPriceToday: Double = repository.getHighestDayPrice(
                year = year,
                month = month,
                day = day,
                priceDistrict = priceDistrict
            )

            _homeUiState.update{ currentState ->
                currentState.copy(
                    currentPrice = currentSpotPrice * 1.25, // mva
                    precentageOfHighestDayPrice = currentSpotPrice / highestPriceToday
                )
            }
            _detailedPriceUiState.update{ currentState ->
                currentState.copy(
                    currentPrice = currentSpotPrice * 1.25, // mva
                )
            }
            _pricePerActivityUiState.update { currentState ->
                currentState.copy(
                    spotPrice = currentSpotPrice * 1.25 // mva
                )
            }

        }
    }
    //This function calls on necessary function to update the UI state with the temperature and weather symbol
    private fun loadForecast() {
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        Log.d("Time", hour.toString())
        //("https://in2000.api.met.no/weatherapi/locationforecast/2.0/compact?lat=59.9114&lon=10.7579")
        viewModelScope.launch {
            _homeUiState.update { currentState ->
                currentState.copy(
                    currentTemp = repository.getCurrentTemp(),
                    weatherSymbolCode = repository.getCurrentSymbol()
                )
            }
        }
    }


    //The function loads the graph with average prices for electricity  given the amount of days already in UI state.
    private fun loadGraphData() {
        _detailedPriceUiState.update { currentState ->
            currentState.copy(
                graphButtonActive = false
            )
        }
        viewModelScope.launch {
            val date = Date()
            val calendar = Calendar.getInstance()
            calendar.time = date
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)  // January is 0
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // if it is prediction
            if (_detailedPriceUiState.value.graphOptions == GraphOptions.Prediction7) {
                val listDouble = repository.getPriceForNextXDays(
                    year = year,
                    month = month,
                    day = day,
                    priceDistrict = priceDistrict,
                    xDays = 7
                )

                val floatList = mutableListOf<Pair<String, Float>>()

                listDouble.forEach {
                    floatList.add(Pair(first = it.first, second = it.second.toFloat()))
                }
                _detailedPriceUiState.update { currentState ->
                    currentState.copy(
                        avgELPriceXDays = floatList,
                        graphButtonActive = true,
                        percentageLastXDays = 0.0
                    )
                }
                return@launch
            }



            // here is GraphOption 7 or 30 days, guaranteed not prediction
            val timeInterval: Int = if (_detailedPriceUiState.value.graphOptions == GraphOptions.Last7Days) {
                7
            } else {
                30
            }

            val avgPriceList = repository.getPriceFromLastXDays(
                year = year,
                month = month,
                day = day,
                xDays = timeInterval,
                priceDistrict = priceDistrict

            ).toMutableList()

            Log.d("Prices", avgPriceList.toString())
            val avgPriceListFloat =  mutableListOf<Pair<String, Float>>()


            avgPriceList.forEach {
                avgPriceListFloat.add(Pair(it.first, it.second.toFloat()))
            }
            Log.d("avgPriceList", avgPriceListFloat.toString())

            var avgPrice = 0.0
            for (pair in avgPriceList) {
                if (pair == avgPriceList.last()) break
                avgPrice += pair.second
            }
            avgPrice /= avgPriceList.size - 1

            _detailedPriceUiState.update { currentState ->
                currentState.copy(
                    avgELPriceXDays = avgPriceListFloat,
                    graphButtonActive = true,
                    percentageLastXDays = ((avgPriceList.last().second - avgPrice) / avgPrice) * 100
                )
            }
        }
    }

    //This changes the time interval for the graph given x days or next x days (prediction)
    fun changeTimeIntervalGraph(graphOption: GraphOptions) {
        if (graphOption == detailedPriceUiState.value.graphOptions) {
            return
        }
        _detailedPriceUiState.update { currentState ->
            currentState.copy(
                graphOptions = graphOption
            )
        }
        loadGraphData()
    }
    //This changes the price district to load different prices given different districts.
    fun changePriceDistrict(newDistrict: String) {
        if (newDistrict == priceDistrict) {
            return
        }
        priceDistrict = newDistrict
        loadStromprisData()
        loadSpotpriceComparison()
        loadGraphData()
    }
}
