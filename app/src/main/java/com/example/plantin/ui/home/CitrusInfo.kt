package com.example.plantin.ui.home


import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantin.R
import com.example.plantin.classification.ClassificationActivity
import com.example.plantin.ui.CitrusDisease
import com.example.plantin.ui.GuideApp
import com.example.plantin.ui.MaintenanceActivity
import com.example.plantin.ui.Treatment.TreatmentActivity

@Composable
fun CitrusInfo() {
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        // Grid 2x2 untuk button
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(120.dp) // Fixed height untuk 2 rows
        ) {
            item {
                DiagnosisButton(
                    text = "Penyakit Jeruk",
                    iconId = R.drawable.img_disease_citrus,
                    onClick = {
                        context.startActivity(Intent(context, CitrusDisease::class.java))
                    }
                )
            }
            item {
                DiagnosisButton(
                    text = "Perawatan Jeruk",
                    iconId = R.drawable.img_citrus_care,
                    onClick = {
                        context.startActivity(Intent(context, MaintenanceActivity::class.java))
                    }
                )
            }
            item {
                DiagnosisButton(
                    text = "Petunjuk Aplikasi",
                    iconId = R.drawable.img_guide,
                    onClick = {
                        context.startActivity(Intent(context, GuideApp::class.java))
                    }
                )
            }
            item {
                DiagnosisButton(
                    text = "Penanganan",
                    iconId = R.drawable.img_main_treatment,
                    onClick = {
                        context.startActivity(Intent(context, TreatmentActivity::class.java))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Cegah penyakit sebelum terlambat",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        DetectionCard(
            onDetectionClick = {
                context.startActivity(Intent(context, ClassificationActivity::class.java))
            }
        )
    }
}

@Composable
fun DetectionCard(onDetectionClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD7E2B6)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Deteksi Tanamanmu",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Text(
                    text = "Sekarang !",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Lihat hasil dan lakukan perawatan",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black.copy(alpha = 0.7f)
                )
            }

            StepIcon(
                iconId = R.drawable.img_camera_detection,
                onClick = onDetectionClick
            )
        }
    }
}

@Composable
fun StepIcon(iconId: Int, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(62.dp)
                .shadow(4.dp, RoundedCornerShape(8.dp)) // Tambahkan shadow
                .background(Color.White, RoundedCornerShape(8.dp))
                .clickable(
                    indication = rememberRipple(color = Color.Gray),
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() }
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "Camera Detection",
                tint = Color.Unspecified,
                modifier = Modifier.size(46.dp)
            )
        }
    }
}


@Composable
fun DiagnosisButton(text: String, iconId: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp) // Lebih fleksibel dari height(40.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = text,
                modifier = Modifier
                    .size(36.dp)
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit
            )

            Text(
                text = text,
                color = Color.Black,
                maxLines = 1, // atau unlimited jika perlu
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 0.dp) // Biar gak mentok ke ujung
            )
        }
    }
}

