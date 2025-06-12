package com.example.plantin.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantin.ui.theme.PlantInTheme

class GuideApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlantInTheme {
                GuideScreen()
            }
        }
    }
}

@Composable
fun GuideScreen() {
    Column(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.statusBars)
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        TopBarGuideApp()

        // Deskripsi
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Petunjuk aplikasi menyediakan petunjuk langkah demi langkah tentang cara menggunakan produk atau sistem dengan benar serta membantu pengguna menghindari kesalahan umum yang dapat terjadi saat menggunakan produk atau sistem untuk pertama kali.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Baca Selengkapnya",
                color = Color(0xFF3F51B5),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { /* Tampilkan detail */ }
            )
        }

        // Tombol Unduh dan Pagination
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { /* handle download */ },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Unduh")
            }
            Text(text = "1 / 2")
        }

        // Kartu Konten
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .weight(1f),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Petunjuk Aplikasi Plantin",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Deskripsi.....")
            }
        }
    }
}

@Composable
fun TopBarGuideApp() {
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp))
                .background(Color(0xFFFAD7BE))
        ) {
            IconButton(
                onClick = { (context as? Activity)?.finish() },
                modifier = Modifier
                    .size(48.dp)
                    .padding(start = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .shadow(elevation = 8.dp, shape = CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFFFAD7BE))
                        .fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Text(
                text = "Petunjuk Aplikasi",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp, end = 48.dp)
            )
        }
    }
}
