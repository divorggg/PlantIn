package com.example.plantin.weather.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantin.weather.model.MyLatlLng
import com.example.plantin.weather.model.forecast.ForecastResult
import com.example.plantin.weather.model.weather.WeatherResult
import com.example.plantin.weather.network.RetrofitClient
import kotlinx.coroutines.launch

enum class STATE {
    LOADING,
    SUCCESS,
    FAILED
}

class WeatherViewmodel : ViewModel() {

    // State management
    var state by mutableStateOf(STATE.LOADING)

    // Hold value from API for weather
    var weatherResponse: WeatherResult by mutableStateOf(WeatherResult())

    // Hold value from API for forecast
    var forecastResponse: ForecastResult by mutableStateOf(ForecastResult())

    // Flag untuk mengecek apakah data sudah diambil
    var isWeatherFetched by mutableStateOf(false)
        private set  // Supaya hanya bisa diubah dari dalam ViewModel

    var errorMessage: String by mutableStateOf("")

    // Variabel untuk menyimpan lokasi saat ini
    var currentLocation by mutableStateOf(MyLatlLng(0.0, 0.0))

    fun getWeatherByLocation(latLng: MyLatlLng) {
        Log.d("WeatherViewmodel", "Fetching weather data...")
        viewModelScope.launch {
            state = STATE.LOADING
            val apiService = RetrofitClient.getInstance()
            try {
                val apiResponse = apiService.getWeather(latLng.lat, latLng.lng)
                weatherResponse = apiResponse
                isWeatherFetched = true  // Tandai bahwa data telah diambil
                state = STATE.SUCCESS
            } catch (e: Exception) {
                errorMessage = e.message.toString()
                state = STATE.FAILED
            }
        }
    }

    fun getForecastByLocation(latLng: MyLatlLng) {
        Log.d("WeatherViewmodel", "Fetching forecast data...")
        viewModelScope.launch {
            state = STATE.LOADING
            val apiService = RetrofitClient.getInstance()
            try {
                val apiResponse = apiService.getForecast(latLng.lat, latLng.lng)
                forecastResponse = apiResponse
                state = STATE.SUCCESS
            } catch (e: Exception) {
                errorMessage = e.message.toString()
                state = STATE.FAILED
            }
        }
    }

    
}
