package com.example.plantin.weather.model.weather

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName ("all") var all : String? = null
)
