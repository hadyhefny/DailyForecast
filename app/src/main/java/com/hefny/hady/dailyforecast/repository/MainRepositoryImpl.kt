package com.hefny.hady.dailyforecast.repository

import android.util.Log
import com.hefny.hady.dailyforecast.api.WeatherApi
import com.hefny.hady.dailyforecast.api.responses.ErrorResponse
import com.hefny.hady.dailyforecast.api.responses.MainResponse
import com.hefny.hady.dailyforecast.api.responses.toForecastEntity
import com.hefny.hady.dailyforecast.persistence.ForecastDao
import com.hefny.hady.dailyforecast.persistence.entities.ForecastEntity
import com.hefny.hady.dailyforecast.persistence.entities.toMainResponse
import com.hefny.hady.dailyforecast.utils.Constants
import com.hefny.hady.dailyforecast.utils.ErrorMessages
import com.hefny.hady.dailyforecast.utils.ErrorUtils
import com.hefny.hady.dailyforecast.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepositoryImpl
@Inject constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val weatherApi: WeatherApi,
    private val forecastDao: ForecastDao
) : MainRepository {
    override suspend fun getWeatherByCityName(city: String): Resource<MainResponse> {
        val resource: Resource<MainResponse>
        withContext(ioDispatcher) {
            resource = try {
                // load data from remote data source
                val mainResponse: MainResponse = weatherApi.getWeatherByCityName(city)
                // clear old data
                if (forecastDao.load(mainResponse.city.name) != null){
                    forecastDao.delete(mainResponse.city.name)
                }
                // save new data to database
                forecastDao.save(mainResponse.toForecastEntity())
                Resource.data(forecastDao.load(city)?.toMainResponse())
            } catch (e: Exception) {
//                Log.e("MainRepositoryImpl", "getWeatherByCityName: ", e)
                // load data from cache
                val forecastEntity: ForecastEntity? = forecastDao.load(city)
                // if cache has data, show it with error message
                if (forecastEntity != null) {
                    val myMainResponse: MainResponse = forecastEntity.toMainResponse()
                    Resource.data(data = myMainResponse, message = ErrorMessages.NOT_ACCURATE_DATA)
                } else { // if cache has no data, show error message
                    val errorResponse: ErrorResponse = ErrorUtils.parseError(e)
                    Resource.error(errorResponse.message)
                }
            }
        }
        return resource
    }
}