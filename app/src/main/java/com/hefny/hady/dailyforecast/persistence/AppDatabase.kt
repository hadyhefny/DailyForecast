package com.hefny.hady.dailyforecast.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hefny.hady.dailyforecast.persistence.entities.ForecastEntity

@Database(
    entities = [ForecastEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao
}