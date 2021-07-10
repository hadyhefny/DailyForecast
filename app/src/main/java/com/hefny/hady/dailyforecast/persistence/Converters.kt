package com.hefny.hady.dailyforecast.persistence

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.hefny.hady.dailyforecast.models.ForecastItem

class Converters {
    @TypeConverter
    fun forecastListToJson(value: List<ForecastItem>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToForecastList(value: String) =
        Gson().fromJson(value, Array<ForecastItem>::class.java).toList()
}