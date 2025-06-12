package com.example.plantin.weather.constant

class Const {
    companion object {


        val permission = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )

        const val openWeatherMapApiKey = "159eb7cf3ad21ffa22a600e9bb2202df"

        const val colorBg1 = 0xFFB3D8A8
        const val colorBg2 = 0xFF3D8D7A
        const val cardColor = 0xFF6C739E

        const val LOADING = "Loading..."
        const val NA = "N/A"
    }
}
