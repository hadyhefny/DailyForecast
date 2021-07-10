package com.hefny.hady.dailyforecast

import com.hefny.hady.dailyforecast.api.responses.MainResponse
import com.hefny.hady.dailyforecast.repository.MainRepository
import com.hefny.hady.dailyforecast.utils.Resource

class FakeMainRepositoryAndroid(private val mainResource: Resource<MainResponse>) : MainRepository {
    private var shouldReturnError: Boolean = false
    private var shouldReturnErrorAndOldData: Boolean = false
    private var shouldReturnLoading: Boolean = false
    override suspend fun getWeatherByCityName(city: String): Resource<MainResponse> {
        return when {
            shouldReturnError -> {
                Resource.error("something went wrong")
            }
            shouldReturnErrorAndOldData -> {
                Resource.data(mainResource.data, "not accurate data")
            }
            shouldReturnLoading->{
                Resource.loading(true)
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
    fun shouldReturnLoading(isLoading: Boolean) {
        shouldReturnLoading = isLoading
    }
}