package com.example.plantin.onboarding


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantin.MainActivity
import com.example.plantin.R
import com.example.plantin.ui.theme.PlantInTheme
import com.example.plantin.weather.ui.theme.SplashTheme
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashTheme {
                SplashScreen {
                    // Navigasi ke MainActivity setelah splash selesai
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val logoScale = remember { Animatable(0f) }
    val logoAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
        logoAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
        delay(1800)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo image
            Image(
                painter = painterResource(id = R.drawable.icon_plantin), // ganti sesuai nama file logo kamu
                contentDescription = "CitrusDetector Logo",
                modifier = Modifier
                    .size(240.dp) // PERBESAR ukuran di sini
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value)
            )

            Spacer(modifier = Modifier.height(24.dp))


        }
    }
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    PlantInTheme {
        SplashScreen {}
    }
}