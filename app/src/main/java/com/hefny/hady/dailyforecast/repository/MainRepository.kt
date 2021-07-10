package com.hefny.hady.dailyforecast.repository

import com.hefny.hady.dailyforecast.api.responses.MainResponse
import com.hefny.hady.dailyforecast.utils.Resource

interface MainRepository {
    suspend fun getWeatherByCityName(city: String): Resource<MainResponse>
}