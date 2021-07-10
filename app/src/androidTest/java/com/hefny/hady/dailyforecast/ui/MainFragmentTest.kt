package com.hefny.hady.dailyforecast.ui

import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
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
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainFragmentTest {
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
    fun init() {
        // Populate @Inject fields in test class
        hiltRule.inject()
    }

    private var mainRepository: MainRepository = FakeMainRepositoryAndroid(mainResource)

    @BindValue
    val viewModel = MainViewModel(mainRepository)

    @Test
    fun searchByCityAndRecyclerViewHasData() {
        val activityScenario = launch(MainActivity::class.java)
        onView(withId(R.id.city_name_edittext)).perform(replaceText(mainResponse.city.name))
        onView(withId(R.id.search_btn)).perform(click())
        onView(withId(R.id.weather_recyclerview)).check(matches(hasDescendant(withText(mainResponse.forecastList[0].weatherDescription[0].description))))
        activityScenario.close()
    }
}