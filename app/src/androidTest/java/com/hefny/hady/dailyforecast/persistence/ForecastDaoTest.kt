package com.hefny.hady.dailyforecast.persistence

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hefny.hady.dailyforecast.models.ForecastItem
import com.hefny.hady.dailyforecast.persistence.entities.ForecastEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ForecastDaoTest {

    private lateinit var database: AppDatabase

    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun saveForecastEntityAndLoadedDataReturnNotNull() = runBlockingTest {
        // GIVEN - ForecastEntity
        val forecastItemList = ArrayList<ForecastItem>()
        val forecastItem = ForecastItem(dateInSeconds = 1625669439, 30.2, 29.5, 35.6, "clear")
        forecastItemList.add(forecastItem)
        val forecastEntity = ForecastEntity(forecastItemList = forecastItemList, cityName = "cairo")

        // WHEN - save ForecastEntity into database
        database.forecastDao().save(forecastEntity)

        // THEN - the loaded data is not null
        val loaded: ForecastEntity? = database.forecastDao().load("cairo")
        assertThat(loaded, notNullValue())
    }

    @Test
    fun saveForecastEntityAndGetByCity() = runBlockingTest {
        // GIVEN - save a ForecastEntity
        val forecastItemList = ArrayList<ForecastItem>()
        val forecastItem = ForecastItem(dateInSeconds = 1625669439, 30.2, 29.5, 35.6, "clear")
        forecastItemList.add(forecastItem)
        val forecastEntity = ForecastEntity(forecastItemList = forecastItemList, cityName = "cairo")
        database.forecastDao().save(forecastEntity)

        // WHEN - load ForecastEntity using city name form the database
        val loaded: ForecastEntity? = database.forecastDao().load("cairo")

        // THEN - the loaded data contains the expected values
        assertThat(loaded, notNullValue())
        assertThat(loaded?.cityName, `is`(forecastEntity.cityName))
        loaded?.forecastItemList?.forEachIndexed { index, myForecastItem ->
            assertThat(
                myForecastItem.dateInSeconds,
                `is`(forecastEntity.forecastItemList[index].dateInSeconds)
            )
            assertThat(
                myForecastItem.description,
                `is`(forecastEntity.forecastItemList[index].description)
            )
            assertThat(myForecastItem.temp, `is`(forecastEntity.forecastItemList[index].temp))
            assertThat(myForecastItem.tempMax, `is`(forecastEntity.forecastItemList[index].tempMax))
            assertThat(myForecastItem.tempMin, `is`(forecastEntity.forecastItemList[index].tempMin))
        }
    }

    @Test
    fun deleteForecastEntityAndLoadedDataReturnNull() = runBlockingTest {
        // GIVEN - save a ForecastEntity
        val forecastItemList = ArrayList<ForecastItem>()
        val forecastItem = ForecastItem(dateInSeconds = 1625669439, 30.2, 29.5, 35.6, "clear")
        forecastItemList.add(forecastItem)
        val forecastEntity = ForecastEntity(forecastItemList = forecastItemList, cityName = "cairo")
        database.forecastDao().save(forecastEntity)

        // WHEN - delete data from database
        database.forecastDao().delete("cairo")

        // THEN - the saved ForecastEntity is deleted
        val loaded: ForecastEntity? = database.forecastDao().load("cairo")
        assertThat(loaded, `is`(nullValue()))
    }

    @Test
    fun deleteForecastEntityAndLoadedDataReturnNotNull() = runBlockingTest {
        // GIVEN - save two ForecastEntity
        val forecastItemList = ArrayList<ForecastItem>()
        val forecastItem = ForecastItem(dateInSeconds = 1625669439, 30.2, 29.5, 35.6, "clear")
        forecastItemList.add(forecastItem)
        val forecastEntity = ForecastEntity(forecastItemList = forecastItemList, cityName = "cairo")
        val forecastItemList1 = ArrayList<ForecastItem>()
        val forecastItem1 = ForecastItem(dateInSeconds = 1625669685, 25.6, 19.5, 27.2, "raining")
        forecastItemList1.add(forecastItem1)
        val forecastEntity1 = ForecastEntity(forecastItemList = forecastItemList1, cityName = "suez")
        database.forecastDao().save(forecastEntity)
        database.forecastDao().save(forecastEntity1)

        // WHEN - delete data from database
        database.forecastDao().delete("cairo")

        // THEN - the loaded data contains the expected values
        val loaded: ForecastEntity? = database.forecastDao().load("suez")
        assertThat(loaded, notNullValue())
        assertThat(loaded?.cityName, `is`(forecastEntity1.cityName))
        loaded?.forecastItemList?.forEachIndexed { index, myForecastItem ->
            assertThat(
                myForecastItem.dateInSeconds,
                `is`(forecastEntity1.forecastItemList[index].dateInSeconds)
            )
            assertThat(
                myForecastItem.description,
                `is`(forecastEntity1.forecastItemList[index].description)
            )
            assertThat(myForecastItem.temp, `is`(forecastEntity1.forecastItemList[index].temp))
            assertThat(myForecastItem.tempMax, `is`(forecastEntity1.forecastItemList[index].tempMax))
            assertThat(myForecastItem.tempMin, `is`(forecastEntity1.forecastItemList[index].tempMin))
        }
    }
}