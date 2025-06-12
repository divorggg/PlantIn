package com.example.plantin.weather.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.plantin.weather.constant.Const.Companion.LOADING
import com.example.plantin.weather.model.weather.WeatherResult
import com.example.plantin.weather.utils.Utils.Companion.BuildIcon
import com.example.plantin.weather.utils.Utils.Companion.timestampToHumanDate
import com.example.plantin.weather.utils.translateWeatherCondition

@Composable
fun WeatherSection(weatherResponse: WeatherResult) {
    /*
    * Title Section
     */
    var title = ""
    if (!weatherResponse.name.isNullOrEmpty()) {
        weatherResponse?.name?.let {
            title = it
        }
    } else {
        weatherResponse.coord?.let {
            title = "${it.lat}/ ${it.lon}"
        }
    }

    /*
    * SubTitle Section
     */
    val dateVal = (weatherResponse.dt ?: 0)
    val subTitle = if (dateVal == 0) LOADING
    else timestampToHumanDate(dateVal.toLong(), "dd/MM/yyyy")

    /*
    * Icon & Description Section
     */
    var icon = ""
    var description = ""
    weatherResponse.weather?.let {
        if (it.isNotEmpty()) {
            description = it[0].description ?: LOADING
            icon = it[0].icon ?: LOADING
        }
    }

    // **Terjemahkan deskripsi cuaca**
    val translatedDescription = translateWeatherCondition(description)

    /*
    * Temp Section
     */
    var temp = ""
    weatherResponse.main?.let {
        temp = "${it.temp} °C"
    }

    WeatherImage(icon = icon)
    WeatherTitleSection(text = temp, subText = translatedDescription, fontSize = 60.sp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }
}


@Composable
fun WeatherImage(icon: String) {
    AsyncImage(
        model = BuildIcon(icon), contentDescription = icon,
        modifier = Modifier
            .width(200.dp)
            .height(200.dp),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
fun WeatherTitleSection(text: String, subText: String, fontSize: TextUnit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text, fontSize = fontSize, color = Color.White, fontWeight = FontWeight.Bold)
        Text(subText, fontSize = 14.sp, color = Color.White)
    }
}

