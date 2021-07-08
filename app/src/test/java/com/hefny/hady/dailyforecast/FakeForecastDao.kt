package com.hefny.hady.dailyforecast

import com.hefny.hady.dailyforecast.persistence.ForecastDao
import com.hefny.hady.dailyforecast.persistence.entities.ForecastEntity

class FakeForecastDao : ForecastDao {
    private val forecastEntityList = ArrayList<ForecastEntity>()
    override suspend fun save(forecastEntity: ForecastEntity) {
        forecastEntityList.add(forecastEntity)
    }

    override suspend fun delete(cityName: String) {
        forecastEntityList.removeIf {
            it.cityName == cityName
        }
    }

    override suspend fun load(cityName: String): ForecastEntity? {
        return forecastEntityList.firstOrNull {
            it.cityName == cityName
        }
    }
}