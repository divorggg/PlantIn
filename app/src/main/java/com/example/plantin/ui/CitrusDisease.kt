package com.example.plantin.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantin.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

class CitrusDisease : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiseaseSliderScreen { finish() }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DiseaseSliderScreen(onBackClick: () -> Unit) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val images = listOf(
        R.drawable.img_blackspot,
        R.drawable.img_citrus_canker,
        R.drawable.img_greening
    )

    val diseaseTitles = listOf(
        "Black Spot",
        "Citrus Canker",
        "Greening"
    )

    val descriptions = listOf(
        Pair(R.string.BlackSpotDesc, R.string.BlackSpotDesc2),
        Pair(R.string.CitrusCankerDesc, R.string.CitrusCankerDesc2),
        Pair(R.string.GreeningDesc, R.string.CitrusGreeningDesc2)
    )

    // Perbarui deskripsi berdasarkan halaman saat ini
    val currentDescription = remember(pagerState.currentPage) {
        descriptions[pagerState.currentPage]
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding() // Menambahkan padding sesuai ukuran navigation bar
            .background(Color(0xFFFAA96C))
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .background(Color.Gray.copy(alpha = 0.3f), shape = CircleShape)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "Penyakit Jeruk",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Image Slider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(250.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                count = images.size,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .align(Alignment.Center)
            ) { page ->
                Image(
                    painter = painterResource(id = images[page]),
                    contentDescription = "Image $page",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium)
                )
            }
            IconButton(
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                },
                enabled = pagerState.currentPage > 0,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Left",
                    modifier = Modifier.size(48.dp),
                    tint = Color.Black
                )
            }
            IconButton(
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                },
                enabled = pagerState.currentPage < images.size - 1,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Right",
                    modifier = Modifier.size(48.dp),
                    tint = Color.Black
                )
            }
        }

        // Pager Indicator
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )

        // Info Card
        Card(
            shape = RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp),
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 16.dp, horizontal = 48.dp)
            ) {
                Text(
                    text = diseaseTitles[pagerState.currentPage], // Gunakan title sesuai page
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = stringResource(id = currentDescription.first), // Menggunakan remember
                    fontSize = 16.sp,
                    style = TextStyle(
                        fontSize = 16.sp,
                        textIndent = TextIndent(firstLine = 16.sp)
                    ),
                    lineHeight = 28.sp,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = stringResource(id = currentDescription.second), // Menggunakan remember
                    fontSize = 16.sp,
                    lineHeight = 28.sp,
                    textAlign = TextAlign.Justify,
                    style = TextStyle(
                        fontSize = 16.sp,
                        textIndent = TextIndent(firstLine = 16.sp)
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

