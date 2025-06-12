package com.example.plantin.weather.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.plantin.weather.constant.Const.Companion.NA
import com.example.plantin.weather.constant.Const.Companion.cardColor
import com.example.plantin.weather.model.forecast.ForecastResult
import com.example.plantin.weather.utils.Utils.Companion.BuildIcon
import com.example.plantin.weather.utils.Utils.Companion.timestampToHumanDate
import kotlinx.coroutines.launch

@Composable
fun ForecastSection(forecastResponse: ForecastResult) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val dailyForecast = forecastResponse.list
        ?.groupBy { timestampToHumanDate(it.dt?.toLong() ?: 0L, "yyyy-MM-dd") }
        ?.mapNotNull { (_, values) ->
            values.minByOrNull {
                // Hitung perbedaan antara jam yang ada dengan jam 12:00
                val hour = timestampToHumanDate(it.dt?.toLong() ?: 0L, "HH").toIntOrNull() ?: 0
                kotlin.math.abs(hour - 12) // Cari yang paling dekat ke jam 12
            }
        }
        ?: emptyList()





    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(9.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF363D68))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            val cityName = forecastResponse.city?.name ?: "Unknown City"
            val country = forecastResponse.city?.country ?: "Unknown Country"
            val dateText = forecastResponse.list?.firstOrNull()?.dt?.let { dt ->
                timestampToHumanDate(dt.toLong(), "EEEE")
            } ?: "Unknown Date"

            Text(text = dateText, color = Color.White)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location Icon",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$cityName, $country",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Divider(color = Color.Gray, thickness = 2.dp, modifier = Modifier.fillMaxWidth())

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            val newIndex = (listState.firstVisibleItemIndex - 1).coerceAtLeast(0)
                            listState.animateScrollToItem(newIndex)
                        }
                    },
                    modifier = Modifier.size(25.dp),
                    enabled = listState.firstVisibleItemIndex > 0
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Scroll Left",
                        tint = if (listState.firstVisibleItemIndex > 0) Color.White else Color.Gray
                    )
                }

                LazyRow(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .height(160.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(dailyForecast) { currentItem ->
                        val time =
                            currentItem.dt?.let { timestampToHumanDate(it.toLong(), "EEE ") }
                                ?: NA
                        val icon = currentItem.weather?.getOrNull(0)?.icon?.let {
                            BuildIcon(
                                it,
                                isBigSize = false
                            )
                        } ?: ""
                        val temp = "${currentItem.main?.temp?.toInt() ?: NA}°C"

                        ForecastTile(temp = temp, image = icon, time = time)
                    }
                }

                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            val newIndex =
                                (listState.firstVisibleItemIndex + 1).coerceAtMost((dailyForecast.size - 1))
                            listState.animateScrollToItem(newIndex)
                        }
                    },

                    modifier = Modifier.size(25.dp),
                    enabled = listState.firstVisibleItemIndex + 3 < dailyForecast.size - 1
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Scroll Right",
                        tint = if (listState.firstVisibleItemIndex + 3 < dailyForecast.size - 1) Color.White else Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun ForecastTile(temp: String, image: String, time: String) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .padding(4.dp)
            .height(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(cardColor),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = time, color = Color.White)
            AsyncImage(
                model = image,
                contentDescription = image,
                modifier = Modifier
                    .width(50.dp)
                    .padding(top = 10.dp)
                    .height(50.dp),
                contentScale = ContentScale.Fit
            )
            Text(text = temp, color = Color.White)
        }
    }
}
