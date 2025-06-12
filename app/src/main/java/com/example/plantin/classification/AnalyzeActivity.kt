package com.example.plantin.classification

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.example.plantin.classification.result.ResultActivity
import com.example.plantin.ui.theme.PlantInTheme

class AnalyzeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageUriString = intent.getStringExtra("croppedImageUri")

        setContent {
            PlantInTheme {
                if (imageUriString != null) {
                    AnalyzeScreen(imageUri = imageUriString.toUri(),
                        onClose = {
                            val intent =
                                Intent(this@AnalyzeActivity, ClassificationActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                            finish()
                        })
                } else {
                    Text("Gambar tidak tersedia")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyzeScreen(imageUri: Uri, onClose: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Analisis Gambar",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .background(Color(0xFFD9D9D9), shape = CircleShape)
                            .size(36.dp) // Ukuran tombol background bulat
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Tutup"
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        val context = LocalContext.current
        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Hasil Gambar",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp)) // Ujung rounded
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(

                onClick = {
                    val intent = Intent(context, ResultActivity::class.java)
                    intent.putExtra("imageUri", imageUri.toString())
                    context.startActivity(intent)
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth(0.6f) // Setengah lebar layar (50%)
                    .height(48.dp)     // Opsional: tinggi tombol
                    .border(
                        1.dp,
                        Color.Black,
                        shape = RoundedCornerShape(50)
                    ), // Garis pinggir hitam
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, // Warna latar tombol
                    contentColor = Color.Black    // Warna teks
                )
            ) {
                Text("Analyze")
            }
            Spacer(modifier = Modifier.height(12.dp))

            Button(

                onClick = {
                    val intent = Intent(context, ClassificationActivity::class.java)
                    context.startActivity(intent)
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth(0.6f) // Setengah lebar layar (50%)
                    .height(48.dp)     // Opsional: tinggi tombol
                    .border(
                        1.dp,
                        Color.Black,
                        shape = RoundedCornerShape(50)
                    ), // Garis pinggir hitam
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, // Warna latar tombol
                    contentColor = Color.Black    // Warna teks
                )
            ) {
                Text("Kembali")
            }
        }
    }
}

