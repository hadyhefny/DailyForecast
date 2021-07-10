package com.hefny.hady.dailyforecast

import com.hefny.hady.dailyforecast.api.responses.MainResponse
import com.hefny.hady.dailyforecast.repository.MainRepository
import com.hefny.hady.dailyforecast.utils.Resource

class FakeMainRepository(private val mainResource: Resource<MainResponse>) : MainRepository {
    private var shouldReturnError: Boolean = false
    private var shouldReturnErrorAndOldData: Boolean = false
    override suspend fun getWeatherByCityName(city: String): Resource<MainResponse> {
        return when {
            shouldReturnError -> {
                Resource.error("something went wrong")
            }
            shouldReturnErrorAndOldData -> {
                Resource.data(mainResource.data, "not accurate data")
            }
            else -> {
                mainResource
            }
        }
    }

    fun shouldReturnError(isError: Boolean) {
        shouldReturnError = isError
    }

    fun shouldReturnErrorAndOldData(isError: Boolean) {
        shouldReturnErrorAndOldData = isError
    }
}