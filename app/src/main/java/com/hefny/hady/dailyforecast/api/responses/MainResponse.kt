package com.hefny.hady.dailyforecast.api.responses

import com.google.gson.annotations.SerializedName
import com.hefny.hady.dailyforecast.models.City
import com.hefny.hady.dailyforecast.models.Forecast
import com.hefny.hady.dailyforecast.persistence.entities.ForecastEntity
import com.hefny.hady.dailyforecast.models.ForecastItem

data class MainResponse(
    @SerializedName("cnt")
    val count: Int? = null,
    @SerializedName("list")
    val forecastList: List<Forecast>,
    @SerializedName("city")
    val city: City
)

fun MainResponse.toForecastEntity(): ForecastEntity {
    val forecastItemList = ArrayList<ForecastItem>()
    forecastList.forEach { forecast ->
        forecastItemList.add(
            ForecastItem(
                dateInSeconds = forecast.dateInSeconds,
                temp = forecast.weatherData.temp,
                tempMax = forecast.weatherData.tempMax,
                tempMin = forecast.weatherData.tempMin,
                description = forecast.weatherDescription[0].description
            )
        )
    }
    return ForecastEntity(
        forecastItemList = forecastItemList,
        cityName = city.name
    )
}