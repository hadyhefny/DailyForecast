package com.hefny.hady.dailyforecast.models

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("name")
    val name: String
)