package com.hefny.hady.dailyforecast.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hefny.hady.dailyforecast.api.responses.MainResponse
import com.hefny.hady.dailyforecast.models.*

@Entity
data class ForecastEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "forecast_list")
    val forecastItemList: List<ForecastItem>,
    @ColumnInfo(name = "city_name")
    val cityName: String
)

fun ForecastEntity.toMainResponse(): MainResponse {
    val forecastList = ArrayList<Forecast>()
    val weatherDescriptionList = ArrayList<WeatherDescription>()
    forecastItemList.forEach { forecastItemEntity ->
        weatherDescriptionList.clear()
        weatherDescriptionList.add(WeatherDescription(description = forecastItemEntity.description))
        forecastList.add(
            Forecast(
                dateInSeconds = forecastItemEntity.dateInSeconds,
                weatherData = WeatherData(
                    temp = forecastItemEntity.temp,
                    tempMax = forecastItemEntity.tempMax,
                    tempMin = forecastItemEntity.tempMin
                ),
                weatherDescription = weatherDescriptionList
            )
        )
    }
    return MainResponse(
        forecastList = forecastList,
        city = City(cityName)
    )
}
