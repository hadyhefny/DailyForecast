package com.hefny.hady.dailyforecast.api.responses

import com.google.gson.annotations.SerializedName
import com.hefny.hady.dailyforecast.models.Forecast

data class MainResponse(
    @SerializedName("cnt")
    val count: Int,
    @SerializedName("list")
    val list: ArrayList<Forecast>
)