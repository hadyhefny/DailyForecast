package com.hefny.hady.dailyforecast.persistence

import androidx.room.*
import com.hefny.hady.dailyforecast.persistence.entities.ForecastEntity

@Dao
interface ForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(forecastEntity: ForecastEntity)

    @Query("DELETE FROM forecastentity WHERE city_name = :cityName")
    suspend fun delete(cityName: String)

    @Query("SELECT * FROM forecastentity WHERE city_name LIKE '%'  || :cityName || '%' COLLATE NOCASE")
    suspend fun load(cityName: String): ForecastEntity
}