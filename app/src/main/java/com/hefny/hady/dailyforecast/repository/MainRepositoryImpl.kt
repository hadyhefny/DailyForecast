package com.hefny.hady.dailyforecast.repository

import android.util.Log
import com.hefny.hady.dailyforecast.api.WeatherApi
import com.hefny.hady.dailyforecast.api.responses.ErrorResponse
import com.hefny.hady.dailyforecast.api.responses.MainResponse
import com.hefny.hady.dailyforecast.utils.ErrorUtils
import com.hefny.hady.dailyforecast.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepositoryImpl
@Inject constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val weatherApi: WeatherApi
) : MainRepository {
    override suspend fun getWeatherByCityName(city: String): Resource<MainResponse> {
        val resource: Resource<MainResponse>
        withContext(ioDispatcher) {
            resource = try {
                val mainResponse: MainResponse = weatherApi.getWeatherByCityName(city)
                Resource.data(mainResponse)
            } catch (e: Exception) {
                Log.e("MainRepo", "error: ", e)
                val errorResponse: ErrorResponse = ErrorUtils.parseError(e)
                Resource.error(errorResponse.message)
            }
        }
        return resource
    }
}