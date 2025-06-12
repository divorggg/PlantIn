package com.example.plantin.weather.model.forecast

import com.example.plantin.weather.model.weather.Clouds
import com.example.plantin.weather.model.weather.Main
import com.example.plantin.weather.model.weather.Sys
import com.example.plantin.weather.model.weather.Weather
import com.example.plantin.weather.model.weather.Wind
import com.google.gson.annotations.SerializedName

data class CustomList(
    @SerializedName("dt") var dt: Int? = null,
    @SerializedName("main") var main: Main? = Main(),
    @SerializedName("weather") var weather: ArrayList<Weather>? = arrayListOf(),
    @SerializedName("clouds") var clouds: Clouds? = Clouds(),
    @SerializedName("wind") var wind: Wind? = Wind(),
    @SerializedName("visibility") var visibility: Int? = null,
    @SerializedName("pop") var pop: Double? = null,
    @SerializedName("sys") var sys: Sys? = Sys(),
    @SerializedName("dt_txt") var dtTxt: String? = null,

)
