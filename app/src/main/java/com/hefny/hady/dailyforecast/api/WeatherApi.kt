package com.hefny.hady.dailyforecast.api

import com.hefny.hady.dailyforecast.api.responses.MainResponse
import com.hefny.hady.dailyforecast.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/forecast")
    suspend fun getWeatherByCityName(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = Constants.API_KEY
    ): MainResponse
}