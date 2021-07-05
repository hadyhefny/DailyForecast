package com.hefny.hady.dailyforecast.ui

import androidx.lifecycle.ViewModel
import com.hefny.hady.dailyforecast.api.WeatherApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherApi: WeatherApi
): ViewModel() {
}