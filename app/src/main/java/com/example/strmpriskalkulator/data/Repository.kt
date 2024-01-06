package com.example.strmpriskalkulator.data

import android.util.Log
import java.util.*

/* To easily test our code, we have interfaces to be able to choose the implementation and use mock data to have unit testing.
* */

interface RepositoryInterface {
    suspend fun getCurrentSpotPrice(year: Int, month: Int, day:Int, currentHour: Int, priceDistrict: String): Double

    suspend fun getSpotpriceComparison(year: Int, month: Int, day:Int, currentHour: Int, priceDistrict: String): Double


    suspend fun getCurrentTemp(): Double


    suspend fun getCurrentSymbol(): String

    suspend fun getHighestDayPrice (year: Int, month: Int, day:Int, priceDistrict: String): Double

    suspend fun getPriceFromLastXDays(
        year: Int,
        month: Int,
        day:Int,
        xDays: Int,
        priceDistrict: String
    ) : List<Pair<String, Double>>

    suspend fun getPriceForNextXDays(
        year: Int,
        month: Int,
        day:Int,
        xDays: Int
        , priceDistrict: String
    ) : List<Pair<String, Double>>
}


// class is open to make tests work
open class Repository(private val stromprisDataSource: DataSourceInterface, private val forecastDataSource: DataSourceInterface) : RepositoryInterface {

    /*getCurrentSpotPrice is used to show the spot-price on main screen. Here we take in the current day as a parameter, to use in the URL to get current data.
    Then from the data object we have a list of ElData objects, that given current hour we return the NOK per kWh.
    * */
    override suspend fun getCurrentSpotPrice(year: Int, month: Int, day:Int, currentHour: Int, priceDistrict: String): Double{
        val urlExtension = "$year/${month.toString().padStart(2, '0')}-" +
                "${day.toString().padStart(2, '0')}_$priceDistrict.json"
        val elData: List<ElData> = stromprisDataSource.fetchHvaKosterStromen(urlExtension)
        Log.d("currentPrice", elData[currentHour].toString())
        return elData[currentHour].NOK_per_kWh
    }

    /* In this function we get the currentSpot-price that is showed on screen, and using Calendar getting data yesterday,
    and getting the average price for yesterday and returning the percentage change from yesterdays average to today's current price.
    * */
    override suspend fun getSpotpriceComparison(
        year: Int,
        month: Int,
        day: Int,
        currentHour: Int,
        priceDistrict: String
    ): Double {
        val currentSpotPrice = getCurrentSpotPrice(
            year = year,
            month = month,
            day = day,
            currentHour = currentHour,
            priceDistrict = priceDistrict
        )
        val cal = Calendar.getInstance()
        cal.set(year, month, day)
        cal.add(Calendar.DAY_OF_MONTH, -1)
        val yesterdayDay = cal.get(Calendar.DAY_OF_MONTH)
        val yesterdayMonth = cal.get(Calendar.MONTH)
        val yesterdayYear = cal.get(Calendar.YEAR)
        //By reducing the Calendar.DAY_OF_MONTH by one we are getting yesterdays data to be then used to get the right URL

        val urlExtension = "$yesterdayYear/${yesterdayMonth.toString().padStart(2, '0')}-" +
                "${yesterdayDay.toString().padStart(2, '0')}_$priceDistrict.json"
        val elData: List<ElData> = stromprisDataSource.fetchHvaKosterStromen(urlExtension)

        //Then the El-price gets summed, and the percentage change calculated.
        var sumElPrice = 0.0
        elData.forEach {
            sumElPrice += it.NOK_per_kWh
        }
        val avgPrice = sumElPrice / 24 // hm double and dividing?

        return ((currentSpotPrice - avgPrice) / avgPrice) * 100
    }

    /*This function is used to get the right timezone from LocationForecast API. By converting from current timezone to Z timezone we get right data on screen.
     */
    // open for testing
    open fun getZTime() : String {
        // Get the current time in UTC
        val utcTimeZone = TimeZone.getTimeZone("UTC")
        val zCalendar = Calendar.getInstance(utcTimeZone)

        val zYear = zCalendar.get(Calendar.YEAR)
        val zMonth = zCalendar.get(Calendar.MONTH) +1
        val zDay = zCalendar.get(Calendar.DAY_OF_MONTH)
        val zHour = zCalendar.get(Calendar.HOUR_OF_DAY)
        val currentTime = "$zYear-${zMonth.toString().padStart(2,'0')}-${zDay.toString().padStart(2,'0')}T${zHour.toString().padStart(2,'0')}:00:00Z"
        Log.d("Time", currentTime)
        // Return the hour of the Z time as a double so that the API call will be correct.
        return currentTime// "$zYear-${zMonth.toString().padStart(2,'0')}-${zDay.toString().padStart(2,'0')}T${zHour.toString().padStart(2,'0')}:00:00Z"
    }

    /* CurrentTemp is used to show a basic representation of weather data on screen.
    * */
    override suspend fun getCurrentTemp(): Double{
        //The API has another timezone thus:
        val currentTime = getZTime()
         val elForecastData : ForecastData= forecastDataSource.fetchForecastData()
        val timeList : List<TimeSeries> = elForecastData.properties.timeseries
        //Getting the time-list from the API and then to find the right object loop through until the time matches from the list
        var currentTemp = 0.0
        for (time in timeList) {
            //Example of time: "time": "2020-06-10T13:00:00Z",
            if (time.time == currentTime) {
                currentTemp = time.data.instant.details.air_temperature
                //Here we have found the same the correct time, thus setting the air temperature to
                break
            }
        }
        Log.d("Temp", currentTemp.toString())
        return currentTemp
    }

    /* This function retrieves the precipitation amount for a date. It generates the time, and fetched forecast data and iterates to find matching time.
    * Then the variable regarding precipitation is set and returned. */
    private suspend fun getPrecipitation(year: Int, month: Int, day:Int, currentHour: Int): Double{
        val currentTime =
            "$year-${month.toString().padStart(2,'0')}-${day.toString().padStart(2,'0')}T${currentHour.toString().padStart(2,'0')}:00:00Z"

        val elForecastData : ForecastData= forecastDataSource.fetchForecastData()
        val timeList : List<TimeSeries> = elForecastData.properties.timeseries
        var currentPrecipitation = 0.0
        for (time in timeList) {
            //Example of time: "time": "2020-06-10T13:00:00Z",
            if (time.time == currentTime) {
                currentPrecipitation = time.data.next_1_hours.details.precipitation_amount
                break
            }
        }
        Log.d("Precipitation", currentPrecipitation.toString())
        return currentPrecipitation
    }

    /* This function retrieves the string for the weather symbol from the API to be used to show the same symbol on screen
    * The code is very similar to the other functions using Location Forecast */
    override suspend fun getCurrentSymbol(): String {
        val currentTime = getZTime()
        Log.d("CurrentTime", currentTime)
        val elForecastData : ForecastData= forecastDataSource.fetchForecastData()
        var weatherSymbol = ""
        val timeList : List<TimeSeries> = elForecastData.properties.timeseries
        for (time in timeList) {
            if (time.time == currentTime) {
                weatherSymbol = time.data.next_1_hours.summary.symbol_code
                break // found the right time, and got the symbol for next hour
            }
        }
        Log.d("Symbol", weatherSymbol)
        return weatherSymbol
    }

    /* This function is used to get the highest price of the day for the circle indicating how beneficial it is to use power.  */
    override suspend fun getHighestDayPrice (year: Int, month: Int, day:Int, priceDistrict: String): Double {
        val urlExtension = "$year/${month.toString().padStart(2, '0')}-" +
                "${day.toString().padStart(2, '0')}_$priceDistrict.json"
        val elData: List<ElData> = stromprisDataSource.fetchHvaKosterStromen(urlExtension)
        var mostExpensiveHour: ElData = elData[0]
        elData.forEach {
            if (it.NOK_per_kWh > mostExpensiveHour.NOK_per_kWh) mostExpensiveHour = it
        }
        return mostExpensiveHour.NOK_per_kWh
    }

    /* This is a larger function used to get data from API to the screen with a graph over the prices*/
    override suspend fun getPriceFromLastXDays(
        year: Int,
        month: Int,
        day:Int,
        xDays: Int,
        priceDistrict: String
    ) : List<Pair<String, Double>> {
        val avgPriceList = mutableListOf<Pair<String, Double>>()
        val cal = Calendar.getInstance()
        cal.set(year, month, day)
        var dagTeller = 0
        do {
            //Given the amount of days we are supposed to show on screen the loop iterates over so many days by retracting one from calendar to go back in time
            val tmpYear = cal.get(Calendar.YEAR)
            val tmpMonth = cal.get(Calendar.MONTH)  + 1// January is 0
            val tmpDay = cal.get(Calendar.DAY_OF_MONTH)

            Log.d("url", "$tmpYear/${tmpMonth.toString().padStart(2, '0')}-" +
                    "${tmpDay.toString().padStart(2, '0')}_$priceDistrict.json")

            val elPrices: List<ElData> = stromprisDataSource.fetchHvaKosterStromen("$tmpYear/${tmpMonth.toString().padStart(2, '0')}-" +
                    "${tmpDay.toString().padStart(2, '0')}_$priceDistrict.json")
            var sumElPrice = 0.0
            for (elPrice in elPrices) {
                sumElPrice += elPrice.NOK_per_kWh * 1.25
            }

            val avgPrice = sumElPrice/elPrices.size
            avgPriceList.add(
                Pair(
                    "${tmpDay.toString().padStart(2, '0')}.${tmpMonth.toString().padStart(2, '0')}",
                    avgPrice
                )
            )
            //Then after we have that day average price we add it with the date to a list for all the x days.
            cal.add(Calendar.DAY_OF_MONTH, -1)
            dagTeller++
        } while (dagTeller < xDays)

        return avgPriceList.reversed()
    }

    /*This function is used for predicting the price. There we need a percentage of one day to the day before prices. This calculates to averages and then the percentage change. */
    private suspend fun getAvgSpotpriceComparison(
        year: Int,
        month: Int,
        day: Int,
        priceDistrict: String
    ): Double {

        val cal = Calendar.getInstance()
        cal.set(year, month, day)
        cal.add(Calendar.DAY_OF_MONTH, -1)
        val yesterdayDay = cal.get(Calendar.DAY_OF_MONTH)
        val yesterdayMonth = cal.get(Calendar.MONTH)
        val yesterdayYear = cal.get(Calendar.YEAR)

        val todayUrlExtension = "$year/${(month).toString().padStart(2, '0')}-${
            day.toString().padStart(2, '0')
        }_$priceDistrict.json"
        val yesterdayUrlExtension =
            "$yesterdayYear/${(yesterdayMonth.toString()).padStart(2, '0')}-" +
                    "${yesterdayDay.toString().padStart(2, '0')}_$priceDistrict.json"

        val todayElData: List<ElData> = stromprisDataSource.fetchHvaKosterStromen(todayUrlExtension)
        val yesterdayElData: List<ElData> =
            stromprisDataSource.fetchHvaKosterStromen(yesterdayUrlExtension)

        var sumTodayElPrice = 0.0
        for (elPrice in todayElData) {
            sumTodayElPrice += elPrice.NOK_per_kWh * 1.25
        }
        //After getting today's average price we then calculates yesterday's to return the percentage change.

        val todayAvgPrice = sumTodayElPrice / 24

        var sumYesterdayElPrice = 0.0
        for (elPrice in yesterdayElData) {
            sumYesterdayElPrice += elPrice.NOK_per_kWh * 1.25
        }
        val yesterdayAvgPrice = sumYesterdayElPrice / 24
        return ((todayAvgPrice - yesterdayAvgPrice) / yesterdayAvgPrice) * 100
    }

    /* This function is to predicting prices for the next x days. Using the percentage change as well as temperature and precipitation values. This is described more below.
    * */
    override suspend fun getPriceForNextXDays(
        year: Int,
        month: Int,
        day:Int,
        xDays: Int
        , priceDistrict: String
    ) : List<Pair<String, Double>> {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR)
        val correctMonth = month + 1


        //var priceList= getPriceFromLastXDays(year, month, day, xDays)
        var slope = getAvgSpotpriceComparison(year, correctMonth, day, priceDistrict)
        Log.d("Log Slope 1 ", slope.toString())
        Log.d("date", year.toString()+correctMonth.toString()+day.toString()+hour.toString())
        slope = 1 + (slope/1000)
        // Or prediction is based on change in prices as well as temperature, with temperature changes we will alter the slope
        Log.d("Log Slope 2", slope.toString())


        //If temperature is between 5 and 25 - slope goes down, else it goes up
        //If it rains prices go down - most production of power in Norway is from water.
        //If there is much power available or if it is ex cold then there will be more people wanting power.
        var supply = 0.0 // Affected mostly by water, not much wind power in Norway. We are not factoring in changes in wind in for example other countries Norway buy power from. If it rains one day we add one, and if it doesn't we take away one
        var demand = 0 // Affected mainly by temperature, either low or very high. If the temperature goes outside or back inside the 5-25 gap then we will change the slope based on this
        var change: Double

        val priceList = mutableListOf<Pair<String, Double>>()
        cal.set(year, month, day)
        var dagTeller = 0
        var newPrice = getCurrentSpotPrice(year, correctMonth, day, hour, priceDistrict) *1.25  // Moms???
        Log.d("Log newprice 1", newPrice.toString())
        val checkTemp = getCurrentTemp()
        var withinTempRange = false
        var winter = false
        if (checkTemp>5 && checkTemp<25) {
            withinTempRange = true
        } else if (checkTemp < -5) {
            winter = true
        }

        do {
            //val tmpYear = cal.get(Calendar.YEAR)
            val tmpMonth = cal.get(Calendar.MONTH) +1// January is 0
            val tmpDay = cal.get(Calendar.DAY_OF_MONTH)

            val currentPrecipitation = getPrecipitation(year,month,day, hour)
            val currentTemp = getCurrentTemp()

            //Supply
            if (supply > -0.1 || supply < 0.1 ) {
                if (currentPrecipitation > 0.0) {
                    supply += 0.01
                } else if (currentPrecipitation == 0.0) {
                    supply -= 0.01

                }
            }
            //Demand
            if (withinTempRange) { //Yesterday was within 5->25
                if (currentTemp <-5) {
                    demand += 2
                    winter = true
                    withinTempRange = false
                } else if (currentTemp<5 || currentTemp > 25) {
                    demand ++
                    winter = false
                    withinTempRange = false
                }
                else {
                    demand = 0
                    winter = false
                    withinTempRange = true
                }
            } else { // Meaning yesterday temp is either below 5 or above 25
                if (winter && currentTemp>-5) { //Situation: used to below -5, now above
                    demand--
                    winter = false
                    if (currentTemp> 5 && currentTemp>25) { //If moved out
                        demand--
                        withinTempRange = true

                    }
                } else if (!winter && currentTemp<-5){ //Over -5 and under 5
                    demand++
                    winter = true
                }
                else {
                    demand = 0
                }
            }
            Log.d("Supply:", supply.toString())
            Log.d("Demand: ", demand.toString())

            change = ((demand - supply)/100)

            if ((slope< 1 && change >0) || (slope > 1 && change<0)) {
                slope += change
            }

            Log.d("Log Slope 1", slope.toString())
            newPrice *= slope //For example
            Log.d("Log newprice 2", newPrice.toString())

            priceList.add(
                Pair(
                    "${tmpDay.toString().padStart(2, '0')}.${tmpMonth.toString().padStart(2, '0')}",
                    newPrice
                )
            )
            cal.add(Calendar.DAY_OF_MONTH, +1)
            dagTeller++
        } while (dagTeller < xDays)

        return priceList // Change this
    }

}