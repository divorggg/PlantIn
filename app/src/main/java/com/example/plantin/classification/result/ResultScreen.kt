package com.example.plantin.classification.result

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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

    // Parse result if not loading and not an error
    val (formattedLabel, confidenceText, description, treatment) = if (!isLoading && !result.startsWith(
            "Error"
        ) && result.contains("|")
    ) {
        val parts = result.split("|")
        val labelRaw = parts[0]
        val confidence = parts.getOrNull(1) ?: ""

        // Cek jika tidak terdeteksi
        if (labelRaw.lowercase() == "unknown" || labelRaw.isBlank()) {
            listOf(
                "Tidak Terdeteksi",
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
                "greening", "grenning" -> "Greening (penyakit huanglongbing) disebabkan oleh bakteri dan menyebar melalui serangga. Buah menjadi hijau tidak merata dan pahit."
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
                    Text(
                        "Hasil",
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
                            .size(36.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            // Image in center
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Gambar jeruk",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Text "Hasil Identifikasi:"
            Text(
                text = "Hasil Identifikasi:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Detection Result
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "Memproses gambar...",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }

                result.startsWith("Error") -> {
                    Text(
                        text = "❌ Terjadi Kesalahan",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = result,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                result.contains("|") -> {
                    // Disease/Fresh result
                    Text(
                        text = formattedLabel,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = when (formattedLabel.lowercase()) {
                            "fresh" -> Color(0xFF4CAF50)
                            "black spot", "canker", "greening" -> Color.Black
                            "tidak terdeteksi" -> Color.Red
                            else -> MaterialTheme.colorScheme.primary
                        },
                        textAlign = TextAlign.Center
                    )
                    if (confidenceText.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = confidenceText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }


                else -> {
                    Text(
                        text = result,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            val context = LocalContext.current
            // Treatment Info Dialog
            if (showTreatment && !isLoading && !result.startsWith("Error") && result.contains("|")) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "📋 Deskripsi",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 20.sp
                        )

                    }
                }
            }

            // Buttons
            // Treatment Button
            if (!isLoading && !result.startsWith("Error") && result.contains("|") && formattedLabel != "Tidak Terdeteksi") {
                Button(
                    onClick = { showTreatment = !showTreatment },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth(0.6f) // Setengah lebar layar (50%)
                        .height(48.dp)     // Opsional: tinggi tombol
                        .border(
                            1.dp,
                            Color.Black,
                            shape = RoundedCornerShape(50)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF054D3B)
                    )
                ) {

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (showTreatment) "Tutup Info" else "info selengkapnya",
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Back Button
            Button(
                onClick = {
                    val intent = Intent(context, TreatmentActivity::class.java)
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
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF054D3B)
                )
            ) {

                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Penanganannya",
                    fontSize = 14.sp
                )
            }
        }
    }
}