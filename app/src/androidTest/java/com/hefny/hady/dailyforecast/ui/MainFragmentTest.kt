package com.hefny.hady.dailyforecast.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.hefny.hady.dailyforecast.FakeMainRepositoryAndroid
import com.hefny.hady.dailyforecast.R
import com.hefny.hady.dailyforecast.api.responses.MainResponse
import com.hefny.hady.dailyforecast.models.City
import com.hefny.hady.dailyforecast.models.Forecast
import com.hefny.hady.dailyforecast.models.WeatherData
import com.hefny.hady.dailyforecast.models.WeatherDescription
import com.hefny.hady.dailyforecast.repository.MainRepository
import com.hefny.hady.dailyforecast.utils.Event
import com.hefny.hady.dailyforecast.utils.Resource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainFragmentTest {

    private lateinit var viewModel: MainViewModel
    private lateinit var mainRepository: MainRepository

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
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun initViewModel() {
        mainRepository = FakeMainRepositoryAndroid(mainResource)
        viewModel = MainViewModel(mainRepository)
    }

    @Test
    fun progressbarDisplayInUi() {
        val activityScenario = launch(MainActivity::class.java)

        onView(withId(R.id.city_name_edittext)).perform(replaceText("cairo"))
        onView(withId(R.id.search_btn)).perform(click())
        onView(withId(R.id.progressDialog)).check(matches(isDisplayed()))
//        onView(withId(R.id.progressDialog)).check(matches(not(isDisplayed())))
        activityScenario.close()
    }
}