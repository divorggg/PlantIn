package com.example.plantin.weather

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.plantin.R
import com.example.plantin.ui.theme.PlantInTheme
import com.example.plantin.weather.constant.Const.Companion.permission
import com.example.plantin.weather.model.MyLatlLng
import com.example.plantin.weather.ui.ForecastSection
import com.example.plantin.weather.ui.WeatherSection
import com.example.plantin.weather.viewmodel.STATE
import com.example.plantin.weather.viewmodel.WeatherViewmodel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.coroutineScope

class WeatherPredictActivity : ComponentActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var weatherViewModel: WeatherViewmodel
    private var locationRequired: Boolean = false

    override fun onResume() {
        super.onResume()
        if (locationRequired) startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 10000
        )
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(3000)
            .setMaxUpdateDelayMillis(100)
            .build()

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLocationClient()
        initViewModel()
        setContent {
            var currentLocation by remember { mutableStateOf(MyLatlLng(0.0, 0.0)) }

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    for (location in p0.locations) {
                        currentLocation = MyLatlLng(location.latitude, location.longitude)
                    }
                }
            }

            PlantInTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LocationScreen(this@WeatherPredictActivity, currentLocation)
                }
            }
        }
    }

    private fun fetchWeatherInformation(
        weatherViewModel: WeatherViewmodel,
        currentLocation: MyLatlLng
    ) {
        weatherViewModel.state = STATE.LOADING
        weatherViewModel.getWeatherByLocation(currentLocation)
        weatherViewModel.getForecastByLocation(currentLocation)
        weatherViewModel.state = STATE.SUCCESS
    }

    private fun initViewModel() {
        weatherViewModel =
            ViewModelProvider(this@WeatherPredictActivity)[WeatherViewmodel::class.java]
    }

    @Composable
    private fun LocationScreen(context: Context, currentLocation: MyLatlLng) {
        val launcherMultiplePermissions = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionMap ->
            val areGranted = permissionMap.values.all { it }
            if (areGranted) {
                locationRequired = true
                startLocationUpdates()
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

        val systemUiController = rememberSystemUiController()
        DisposableEffect(true) {
            systemUiController.isSystemBarsVisible = false
            onDispose { systemUiController.isSystemBarsVisible = true }
        }

        LaunchedEffect(currentLocation) {
            coroutineScope {
                if (permission.all {
                        ContextCompat.checkSelfPermission(
                            context,
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    }) {
                    startLocationUpdates()
                } else {
                    launcherMultiplePermissions.launch(permission)
                }
            }
        }

        LaunchedEffect(true) { fetchWeatherInformation(weatherViewModel, currentLocation) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val bitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_img)
                    drawImage(bitmap.asImageBitmap())
                },
            contentAlignment = Alignment.TopCenter

        ) {


            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = weatherViewModel.state == STATE.LOADING,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
                when (weatherViewModel.state) {
                    STATE.FAILED -> ErrorSection(weatherViewModel.errorMessage)
                    else -> {
                        WeatherSection(weatherViewModel.weatherResponse)
                        ForecastSection(weatherViewModel.forecastResponse)
                    }
                }
            }
            FloatingActionButton(
                onClick = { fetchWeatherInformation(weatherViewModel, currentLocation) },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
            }
        }
    }

    @Composable
    private fun ErrorSection(errorMessage: String) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = errorMessage, color = Color.White)
        }
    }

    private fun initLocationClient() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }
}
