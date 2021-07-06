package com.hefny.hady.dailyforecast.ui

import androidx.lifecycle.*
import com.hefny.hady.dailyforecast.api.responses.MainResponse
import com.hefny.hady.dailyforecast.repository.MainRepository
import com.hefny.hady.dailyforecast.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val cityName = MutableLiveData<String>()

    fun setCityName(city: String) {
        cityName.value = city
    }

    val currentWeatherData = Transformations.switchMap(cityName) { city ->
        liveData {
            emit(Resource.loading<MainResponse>(true))
            emit(mainRepository.getWeatherByCityName(city))
        }
    }
}