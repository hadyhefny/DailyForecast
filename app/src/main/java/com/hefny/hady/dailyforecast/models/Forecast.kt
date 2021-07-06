package com.hefny.hady.dailyforecast.models

import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("dt")
    val timeInSeconds: Long,
    @SerializedName("main")
    val weatherData: WeatherData,
    @SerializedName("weather")
    val weatherDescription: ArrayList<WeatherDescription>
)