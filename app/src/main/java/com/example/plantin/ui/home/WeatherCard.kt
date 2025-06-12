package com.example.plantin.ui.home

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.plantin.R
import com.example.plantin.weather.WeatherPredictActivity
import com.example.plantin.weather.constant.Const.Companion.LOADING
import com.example.plantin.weather.model.weather.WeatherResult
import com.example.plantin.weather.utils.Utils.Companion.BuildIcon
import com.example.plantin.weather.utils.translateWeatherCondition

@Composable
fun WeatherCard(weatherResponse: WeatherResult) {
    val context = LocalContext.current


    // Ambil icon jika tersedia, atau gunakan default LOADING
    val icon = weatherResponse.weather?.firstOrNull()?.icon ?: LOADING

    // Ambil data cuaca dengan nilai default jika null
    val temperature = weatherResponse.main?.temp?.let { "$it°C" } ?: "N/A"
    val cityName = weatherResponse.name ?: "Unknown Location"
    var description = weatherResponse.weather?.firstOrNull()?.description ?: "No Description"

    val translatedDescription = translateWeatherCondition(description)

    Card(
        onClick = {
            val intent = Intent(context, WeatherPredictActivity::class.java)
            context.startActivity(intent)
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxWidth()
            .height(185.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(R.drawable.bg_weather),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Weather Icon (di kanan atas)
            WeatherImage(
                icon = icon,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(y = (-35).dp, x = 16.dp)
            )

            // Informasi cuaca
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = temperature,
                    fontSize = 40.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = cityName,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.weight(1f)) // Push text ke bawah
                Text(
                    text = translatedDescription,
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun WeatherImage(icon: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = BuildIcon(icon),
        contentDescription = icon,
        modifier = modifier.size(180.dp),
        contentScale = ContentScale.FillBounds
    )
}
