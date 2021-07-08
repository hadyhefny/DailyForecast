package com.hefny.hady.dailyforecast.repository

import com.hefny.hady.dailyforecast.FakeForecastDao
import com.hefny.hady.dailyforecast.FakeWeatherApi
import com.hefny.hady.dailyforecast.MainCoroutineRule
import com.hefny.hady.dailyforecast.api.responses.MainResponse
import com.hefny.hady.dailyforecast.api.responses.toForecastEntity
import com.hefny.hady.dailyforecast.models.City
import com.hefny.hady.dailyforecast.models.Forecast
import com.hefny.hady.dailyforecast.models.WeatherData
import com.hefny.hady.dailyforecast.models.WeatherDescription
import com.hefny.hady.dailyforecast.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainRepositoryImplTest {

    private val weatherDescription = WeatherDescription("clear")
    private val weatherDescriptionList = listOf(weatherDescription)
    private val forecast = Forecast(
        1625669439,
        WeatherData(
            30.2,
            29.5,
            35.6
        ),
        weatherDescriptionList
    )
    private val forecastList = listOf(forecast)
    private val mainResponse = MainResponse(forecastList = forecastList, city = City("cairo"))

    lateinit var weatherApi: FakeWeatherApi
    lateinit var forecastDao: FakeForecastDao

    // Class under test
    private lateinit var mainRepository: MainRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        weatherApi = FakeWeatherApi(mainResponse)
        forecastDao = FakeForecastDao()
        mainRepository = MainRepositoryImpl(Dispatchers.Main, weatherApi, forecastDao)
    }

    @Test
    fun getWeatherByCityNameReturnMainResponse() = mainCoroutineRule.runBlockingTest {
        // WHEN - mainResponse is requested from the repository
        val myMainResponseResource: Resource<MainResponse> =
            mainRepository.getWeatherByCityName(mainResponse.city.name)
        // THEN - main response is loaded
        assertThat(myMainResponseResource.data!!.peekContent(), IsEqual(mainResponse))
    }

    @Test
    fun getWeatherByCityNameWithNoDataInDatabaseReturnErrorMessage() = mainCoroutineRule.runBlockingTest {
        // GIVEN - there is a server error
        weatherApi.shouldReturnError(true)
        // WHEN - mainResponse is requested from the repository with simulated error
        val myMainResponseResource: Resource<MainResponse> =
            mainRepository.getWeatherByCityName(mainResponse.city.name)
        // THEN - data is null and error message exists
        assertThat(myMainResponseResource.data, `is`(nullValue()))
        assertThat(myMainResponseResource.error?.peekContent(), `is`(notNullValue()))
    }

    @Test
    fun getWeatherByCityNameWithDataInDatabaseReturnDataAndErrorMessage() = mainCoroutineRule.runBlockingTest {
        // GIVEN - data exist in database and there is a server error
        forecastDao.save(mainResponse.toForecastEntity())
        weatherApi.shouldReturnError(true)

        // WHEN - mainResponse is requested from the repository with simulated error
        val myMainResponseResource: Resource<MainResponse> =
            mainRepository.getWeatherByCityName(mainResponse.city.name)
        // THEN - data is not null and error message exists
        assertThat(myMainResponseResource.data, `is`(notNullValue()))
        myMainResponseResource.data?.peekContent()?.forecastList?.forEachIndexed { index, forecast ->
            assertThat(forecast.dateInSeconds, `is`(mainResponse.forecastList[index].dateInSeconds))
            assertThat(forecast.weatherDescription[0].description, `is`(mainResponse.forecastList[index].weatherDescription[0].description))
            assertThat(forecast.weatherData.temp, `is`(mainResponse.forecastList[index].weatherData.temp))
            assertThat(forecast.weatherData.tempMax, `is`(mainResponse.forecastList[index].weatherData.tempMax))
            assertThat(forecast.weatherData.tempMin, `is`(mainResponse.forecastList[index].weatherData.tempMin))
        }
        assertThat(myMainResponseResource.data?.peekContent()?.city?.name, `is`(mainResponse.city.name))
        assertThat(myMainResponseResource.message, `is`(notNullValue()))
        assertThat(myMainResponseResource.error, `is`(nullValue()))
    }
}