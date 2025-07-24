package com.example.plantin.classification.result

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.plantin.ui.Treatment.TreatmentActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    imageUri: Uri,
    result: String,
    isLoading: Boolean = false,
    onClose: () -> Unit
) {
    var showTreatment by remember { mutableStateOf(false) }

    val (formattedLabel, confidenceText, description, treatment) = if (!isLoading && !result.startsWith(
            "Error"
        ) && result.contains("|")
    ) {
        val parts = result.split("|")
        val labelRaw = parts[0]
        val confidence = parts.getOrNull(1) ?: ""

        if (labelRaw.lowercase() == "unknown" || labelRaw.isBlank()) {
            listOf(
                "Tidak Diketahui",
                "",
                "Jeruk tidak dapat dikenali. Coba ambil gambar yang lebih jelas.",
                "Ulangi pengambilan gambar dengan pencahayaan yang cukup."
            )
        } else {
            val formatted = when (labelRaw.lowercase()) {
                "black spot", "blackspot" -> "Black Spot"
                "canker" -> "Canker"
                "fresh" -> "Fresh"
                "greening", "grenning" -> "Greening"
                else -> "Tidak Diketahui"
            }

            val desc = when (labelRaw.lowercase()) {
                "black spot", "blackspot" -> "Black Spot disebabkan oleh jamur Phyllosticta citricarpa. Gejalanya berupa bercak hitam pada permukaan kulit jeruk."
                "canker" -> "Canker disebabkan oleh bakteri Xanthomonas citri, menimbulkan luka seperti keropeng pada kulit buah dan daun."
                "fresh" -> "Buah jeruk terlihat segar dan sehat, tidak menunjukkan gejala penyakit."
                "greening", "grenning" -> "Greening disebabkan oleh bakteri yang menyebar lewat serangga. Buah menjadi hijau tidak merata dan pahit."
                else -> "Deskripsi tidak tersedia."
            }

            val treat = when (labelRaw.lowercase()) {
                "black spot", "blackspot" -> "Buang buah yang terinfeksi dan gunakan fungisida tembaga sesuai dosis anjuran."
                "canker" -> "Pangkas bagian yang terinfeksi dan semprot dengan bakterisida. Gunakan varietas tahan penyakit."
                "fresh" -> "Tidak memerlukan penanganan. Buah dalam kondisi baik."
                "greening", "grenning" -> "Cabut tanaman terinfeksi dan kendalikan vektor serangga dengan insektisida selektif."
                else -> "Penanganan tidak tersedia."
            }

            listOf(formatted, confidence, desc, treat)
        }
    } else {
        listOf("", "", "", "")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Hasil", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Gambar jeruk",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Hasil Identifikasi:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            when {
                isLoading -> {
                    CircularProgressIndicator()
                    Text("Memproses gambar...", style = MaterialTheme.typography.bodyLarge)
                }

                result.startsWith("Error") -> {
                    Text(
                        text = "❌ Terjadi Kesalahan",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(result, textAlign = TextAlign.Center)
                }

                result.contains("|") -> {
                    Text(
                        text = formattedLabel,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = when (formattedLabel.lowercase()) {
                            "fresh" -> Color(0xFF4CAF50)
                            "black spot", "canker", "greening" -> Color.Black
                            "tidak diketahui" -> Color.Red
                            else -> MaterialTheme.colorScheme.primary
                        }
                    )
                    if (formattedLabel == "Tidak Diketahui") {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tidak terdeteksi jenis penyakit yang diketahui. Silakan coba lagi.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    Text(result, textAlign = TextAlign.Center)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            val context = LocalContext.current

            if (showTreatment && formattedLabel != "Tidak Diketahui") {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("📋 Deskripsi", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(description, lineHeight = 20.sp)
                    }
                }
            }

            // Tombol info & penanganan hanya jika label valid
            if (formattedLabel != "Tidak Terdeteksi" && formattedLabel != "Tidak Diketahui" && !isLoading) {
                Button(
                    onClick = { showTreatment = !showTreatment },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(48.dp)
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(50)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF054D3B))
                ) {
                    Text(
                        text = if (showTreatment) "Tutup Info" else "Info Selengkapnya",
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val intent = Intent(context, TreatmentActivity::class.java)
                        context.startActivity(intent)
                    },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(48.dp)
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(50)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF054D3B))
                ) {
                    Text("Penanganannya", fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Tombol kembali jika hasil tidak diketahui
            if (formattedLabel == "Tidak Diketahui" && !isLoading) {
                Button(
                    onClick = onClose,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Kembali", fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
