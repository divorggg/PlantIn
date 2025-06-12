package com.example.plantin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import com.example.plantin.classification.history.HistoryActivity
import com.example.plantin.notes.NotesPage
import com.example.plantin.onboarding.OnBoardingActivity
import com.example.plantin.onboarding.OnboardingDataStore
import com.example.plantin.ui.home.ArticleListScreen
import com.example.plantin.ui.home.CitrusInfo
import com.example.plantin.ui.home.WeatherCard
import com.example.plantin.ui.sidebar.Sidebar
import com.example.plantin.ui.theme.PlantInTheme
import com.example.plantin.weather.constant.Const.Companion.permission
import com.example.plantin.weather.model.MyLatlLng
import com.example.plantin.weather.viewmodel.STATE
import com.example.plantin.weather.viewmodel.WeatherViewmodel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
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
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
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
        lifecycleScope.launch {
            val isOnboardingCompleted = OnboardingDataStore.isOnboardingCompleted(this@MainActivity)
            if (!isOnboardingCompleted) {
                startActivity(Intent(this@MainActivity, OnBoardingActivity::class.java))
                finish()
            } else {
                initializeMainActivity()
            }
        }
    }

    private fun initializeMainActivity() {
        initLocationClient()
        initViewModel()
        enableEdgeToEdge()
        setContent {
            var currentLocation by remember { mutableStateOf(MyLatlLng(0.0, 0.0)) }
            var locationFetched by remember { mutableStateOf(false) }

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    for (location in result.locations) {
                        currentLocation = MyLatlLng(location.latitude, location.longitude)
                    }
                    locationFetched = true
                }
            }

            PlantInTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(Modifier.padding(innerPadding))
                    LocationScreen(this@MainActivity, currentLocation, locationFetched)
                }
            }
        }
    }

    @Composable
    private fun LocationScreen(
        context: Context,
        currentLocation: MyLatlLng,
        locationFetched: Boolean
    ) {
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

        LaunchedEffect(Unit) {
            if (permission.all {
                    ContextCompat.checkSelfPermission(
                        context,
                        it
                    ) == PackageManager.PERMISSION_GRANTED
                }) {
                locationRequired = true
                startLocationUpdates()
            } else {
                launcherMultiplePermissions.launch(permission)
            }
        }

        LaunchedEffect(locationFetched) {
            if (locationFetched) {
                fetchWeatherInformation(weatherViewModel, currentLocation)
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
        weatherViewModel = ViewModelProvider(this)[WeatherViewmodel::class.java]
    }

    private fun initLocationClient() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen(modifier: Modifier = Modifier) {
        val context = LocalContext.current
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedMenu by remember { mutableStateOf("Beranda") }
        var isRefreshing by remember { mutableStateOf(false) }
        val scrollState = rememberScrollState()

        fun refreshData() {
            isRefreshing = true
            fetchWeatherInformation(weatherViewModel, MyLatlLng(0.0, 0.0))
            isRefreshing = false
        }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                Sidebar(selectedMenu = selectedMenu) { menu ->
                    selectedMenu = menu
                    scope.launch { drawerState.close() }
                    when (menu) {
                        "Beranda" -> context.startActivity(
                            Intent(
                                context,
                                MainActivity::class.java
                            )
                        )

                        "Catatan" -> context.startActivity(Intent(context, NotesPage::class.java))
                        "Hasil Scan" -> context.startActivity(
                            Intent(
                                context,
                                HistoryActivity::class.java
                            )
                        )
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(buildAnnotatedString {
                                withStyle(SpanStyle(color = Color(0xFFFAA96C))) { append("Plant") }
                                withStyle(SpanStyle(color = Color(0xFF054D3B))) { append("In") }
                            }, fontWeight = FontWeight.Bold)
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = null)
                            }
                        },
                        actions = {
                            IconButton(onClick = {}) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_logo_mainscreen),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, Color.LightGray, CircleShape)
                                        .shadow(12.dp, CircleShape)
                                )
                            }
                        }
                    )
                }
            ) { padding ->
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = { refreshData() }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        // Konten home kamu
                        WeatherCard(weatherViewModel.weatherResponse)
                        CitrusInfo()
                        ArticleListScreen()
                        YoutubeSection()

                    }
                }
            }
        }
    }
}

@Composable
fun YoutubeSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .background(Color(0xFFF4A261))
            .padding(16.dp)
    ) {
        Text(
            text = "Youtube",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF575655)
        )
        androidx.compose.material3.Divider(
            color = Color(0xFF6C739E).copy(0.7f),
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp) // Memberi jarak agar lebih rapi
        )

        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            YoutubeItem(
                videoId = "Kf4xaJnbkxk",
                title = "BUDIDAYA JERUK KEPROK SIEM PART 1 - NDK Bibit Tanaman",
                thumbnailUrl = "https://img.youtube.com/vi/Kf4xaJnbkxk/0.jpg"
            )

            YoutubeItem(
                videoId = "z9vkL85otGg",
                title = "BUDIDAYA JERUK KEPROK SIEM PART 2 - NDK Bibit Tanaman",
                thumbnailUrl = "https://img.youtube.com/vi/z9vkL85otGg/0.jpg"
            )

            YoutubeItem(
                videoId = "1MS_ARKqEV8",
                title = "JERUK KEPROK MADU, TERIGAS, CHOKUN, BW",
                thumbnailUrl = "https://img.youtube.com/vi/1MS_ARKqEV8/0.jpg"
            )

            YoutubeItem(
                videoId = "WAwl4ySNwMI",
                title = "BUDIDAYA JERUK KEPROK",
                thumbnailUrl = "https://img.youtube.com/vi/WAwl4ySNwMI/0.jpg"
            )

            YoutubeItem(
                videoId = "o6cRbULNgME",
                title = "Budidaya Jeruk Keprok Khas Garut",
                thumbnailUrl = "https://img.youtube.com/vi/o6cRbULNgME/0.jpg"
            )

            YoutubeItem(
                videoId = "v8-T4Haqa0o",
                title = "RAHASIA PANEN 200 KILO PER POHON JERUK KEPROK BATU 55",
                thumbnailUrl = "https://img.youtube.com/vi/v8-T4Haqa0o/0.jpg"
            )

            YoutubeItem(
                videoId = "vNN8qN63ugQ",
                title = "Panduan Penerapan SNI Jeruk Keprok",
                thumbnailUrl = "https://img.youtube.com/vi/vNN8qN63ugQ/0.jpg"
            )

            // Ini hanya testing push dari Android Studio

        }
    }
}

@Composable
fun YoutubeItem(videoId: String, title: String, thumbnailUrl: String) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(end = 16.dp)
            .width(160.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontSize = 10.sp,
                maxLines = 2,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .heightIn(min = 32.dp) // atur tinggi minimum untuk area text
            )

            Image(
                painter = rememberAsyncImagePainter(thumbnailUrl),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Button(
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=$videoId")
                    )
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .height(36.dp)
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF054D3B)),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "Tonton Video",
                    color = Color.White,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

