package com.hefny.hady.dailyforecast

import com.hefny.hady.dailyforecast.api.WeatherApi
import com.hefny.hady.dailyforecast.api.responses.MainResponse
import java.io.IOException

class FakeWeatherApi(val mainResponse: MainResponse) : WeatherApi {
    private var shouldReturnError: Boolean = false
    override suspend fun getWeatherByCityName(cityName: String, apiKey: String): MainResponse {
        if (shouldReturnError) {
            throw IOException("something went wrong")
        } else {
            return mainResponse
        }
    }

    fun shouldReturnError(isError: Boolean) {
        shouldReturnError = isError
    }
}