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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    // Animasi untuk logo
    val logoScale = remember { Animatable(0f) }
    val logoAlpha = remember { Animatable(0f) }

    // Animasi untuk teks
    val textAlpha = remember { Animatable(0f) }
    val textTranslationY = remember { Animatable(50f) }

    LaunchedEffect(Unit) {
        // Animasi logo muncul dengan scale dan fade in
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            )
        )

        logoAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            )
        )

        // Delay sebentar sebelum text muncul
        delay(300)

        // Animasi text muncul dengan fade in dan slide up
        textAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 600,
                easing = FastOutSlowInEasing
            )
        )

        textTranslationY.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = 600,
                easing = FastOutSlowInEasing
            )
        )

        // Tunggu sebentar sebelum pindah ke halaman selanjutnya
        delay(1500)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF4CAF50), // Hijau terang
                        Color(0xFF2E7D32), // Hijau gelap
                        Color(0xFF1B5E20)  // Hijau sangat gelap
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo dengan animasi
            Image(
                painter = painterResource(id = R.drawable.icon_plantin), // Ganti dengan logo Anda
                contentDescription = "PlantIn Logo",
                modifier = Modifier
                    .size(120.dp)
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Nama aplikasi dengan animasi
            Text(
                text = "PlantIn",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(textAlpha.value)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline dengan animasi
            Text(
                text = "Your Plant Care Companion",
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(textAlpha.value)
            )

            Spacer(modifier = Modifier.height(48.dp))

            
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