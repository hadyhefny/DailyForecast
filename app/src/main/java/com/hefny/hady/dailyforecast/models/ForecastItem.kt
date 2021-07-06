package com.hefny.hady.dailyforecast.models

data class ForecastItem(
    val dateInSeconds: Long,
    val temp: Double,
    val tempMin: Double,
    val tempMax: Double,
    val description: String
)