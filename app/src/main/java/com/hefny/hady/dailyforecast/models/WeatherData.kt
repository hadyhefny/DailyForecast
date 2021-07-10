package com.hefny.hady.dailyforecast.models

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double
)