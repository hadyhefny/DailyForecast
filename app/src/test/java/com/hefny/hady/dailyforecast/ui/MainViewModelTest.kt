package com.hefny.hady.dailyforecast.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hefny.hady.dailyforecast.FakeMainRepository
import com.hefny.hady.dailyforecast.MainCoroutineRule
import com.hefny.hady.dailyforecast.api.responses.MainResponse
import com.hefny.hady.dailyforecast.getOrAwaitValue
import com.hefny.hady.dailyforecast.models.City
import com.hefny.hady.dailyforecast.models.Forecast
import com.hefny.hady.dailyforecast.models.WeatherData
import com.hefny.hady.dailyforecast.models.WeatherDescription
import com.hefny.hady.dailyforecast.utils.Event
import com.hefny.hady.dailyforecast.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private lateinit var mainRepository: FakeMainRepository
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
    private val mainResource = Resource(false, null, Event(mainResponse))

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun init() {
        mainRepository = FakeMainRepository(mainResource)
        viewModel = MainViewModel(mainRepository)
    }

    @Test
    fun testSetCityNameTriggerWeatherData(){
        // WHEN - set city name
        viewModel.setCityName(mainResponse.city.name)
        // THEN - weather data live data is triggered
        val value = viewModel.currentWeatherData.getOrAwaitValue()
        assertThat(value, `is`(notNullValue()))
    }

    @Test
    fun getCurrentWeatherData() = mainCoroutineRule.runBlockingTest {
        // WHEN - set city name
        viewModel.setCityName(mainResponse.city.name)
        // THEN - weather data live data is triggered
        val value = viewModel.currentWeatherData
        assertThat(value.getOrAwaitValue().loading, `is`(true))
        assertThat(value.getOrAwaitValue().loading, `is`(false))
        assertThat(value.getOrAwaitValue().data, `is`(notNullValue()))
        assertThat(value.getOrAwaitValue().data?.peekContent(), `is`(mainResponse))
    }

    @Test
    fun getCurrentWeatherDataCauseErrorToDisplay() = mainCoroutineRule.runBlockingTest {
        // GIVEN - main repository throws an error
        mainRepository.shouldReturnError(true)
        // WHEN - set city name
        viewModel.setCityName(mainResponse.city.name)
        // THEN - weather data live data is triggered and error is returned
        val value = viewModel.currentWeatherData
        assertThat(value.getOrAwaitValue().loading, `is`(true))
        assertThat(value.getOrAwaitValue().loading, `is`(false))
        assertThat(value.getOrAwaitValue().data, `is`(nullValue()))
        assertThat(value.getOrAwaitValue().error?.peekContent(), `is`(notNullValue()))
    }

    @Test
    fun getCurrentWeatherDataCauseErrorToDisplayAndOldData() = mainCoroutineRule.runBlockingTest {
        // GIVEN - main repository throws an error
        mainRepository.shouldReturnErrorAndOldData(true)
        // WHEN - set city name
        viewModel.setCityName(mainResponse.city.name)
        // THEN - weather data live data is triggered and error is returned
        val value = viewModel.currentWeatherData
        assertThat(value.getOrAwaitValue().loading, `is`(true))
        assertThat(value.getOrAwaitValue().loading, `is`(false))
        assertThat(value.getOrAwaitValue().data, `is`(notNullValue()))
        assertThat(value.getOrAwaitValue().message, `is`(notNullValue()))
    }
}