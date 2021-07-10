package com.hefny.hady.dailyforecast.models

import com.google.gson.annotations.SerializedName

data class WeatherDescription(
    @SerializedName("description")
    val description: String
)