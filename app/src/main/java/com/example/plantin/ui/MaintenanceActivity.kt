package com.example.plantin.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.plantin.ui.theme.PlantInTheme

data class CardData(
    val icon: ImageVector,
    val title: String,
    val description: String
)

class MaintenanceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlantInTheme {
                MaintenanceScreen()
            }
        }
    }
}


@Composable
fun MaintenanceScreen() {
    // Placeholder sederhana dulu
    Column(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.statusBars)
            .fillMaxSize()
    ) {
        TopBarMaintenance()
        PlantCareScreen()


    }
}

@Composable
fun PlantCareScreen() {
    val cardList = listOf(
        CardData(
            Icons.Default.InvertColors,
            "Penyiraman",
            "2 kali sehari, terutama saat pertumbuhan tunas baru, pembungaan, dan pembentukan buah."
        ),
        CardData(
            Icons.Default.WbSunny,
            "Penyiangan",
            "Dilakukan secara teratur, terutama saat tanaman sudah dewasa."
        ),
        CardData(
            Icons.Default.Eco,
            "Pemupukan",
            "Dilakukan saat tanaman berumur satu bulan dengan pupuk N untuk mempercepat pertumbuhan."
        ),
        CardData(
            Icons.Default.ContentCut,
            "Pemangkasan",
            "Pemangkasan cabang yang tidak diinginkan dilakukan untuk membentuk kerangka percabangan yang optimal."
        ),
        CardData(
            Icons.Default.BugReport,
            "Pengendalian Penyakit",
            "Pengendalian hama dan penyakit perlu dilakukan secara rutin untuk menjaga kesehatan pohon jeruk."
        )
    )

    val detailMap = mapOf(
        "Penyiraman" to listOf(
            "Penyiraman dilakukan secara rutin, terutama saat musim kemarau, dengan metode basin atau alur.",
            "Penyiraman bisa dilakukan 2 kali sehari, terutama saat pertumbuhan tunas baru, pembungaan, dan pembentukan buah.",
            "Sistem pengairan harus tersedia dengan baik untuk memenuhi kebutuhan air tanaman yang semakin besar."
        ),
        "Penyiangan" to listOf(
            "Penyiangan atau pembersihan gulma dilakukan secara teratur, terutama saat tanaman sudah dewasa.",
            "Pemangkasan cabang yang tidak diinginkan juga penting untuk memastikan nutrisi terserap oleh batang utama."
        ),
        "Pemupukan" to listOf(
            "Pemupukan dilakukan saat tanaman berumur satu bulan dengan pupuk N untuk mempercepat pertumbuhan.",
            "Selanjutnya, pemupukan dilakukan setiap 6 bulan hingga satu tahun sekali dengan pupuk NPK.",
            "Saat pertunasan, pupuk NPK daun dapat diberikan untuk mendukung proses pertunasan.",
            "Saat tanaman memasuki usia produktif, pemupukan perlu ditingkatkan dengan pupuk organik dan hayati."
        ),
        "Pemangkasan" to listOf(
            "Pemangkasan cabang yang tidak diinginkan dilakukan untuk membentuk kerangka percabangan yang optimal.",
            "Pemangkasan juga membantu mencegah kerusakan dan penyakit pada pohon jeruk."
        ),
        "Pengendalian Penyakit" to listOf(
            "Pengendalian hama dan penyakit perlu dilakukan secara rutin untuk menjaga kesehatan pohon jeruk.",
            "Penyakit seperti jamur atau bakteri dan hama seperti kutu daun atau tungau dapat menyerang tanaman jeruk.",
            "Memperhatikan tanda-tanda serangan hama dan penyakit, serta mengambil langkah-langkah pencegahan atau pengendalian, sangat penting."
        )

    )

    var showDialog by remember { mutableStateOf(false) }
    var selectedCard by remember { mutableStateOf<CardData?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cardList) { card ->
                PlantCareCard(card) {
                    selectedCard = card
                    showDialog = true
                }
            }
        }

        if (showDialog && selectedCard != null) {
            PlantCareDialog(
                card = selectedCard!!,
                items = detailMap[selectedCard!!.title] ?: listOf("Tidak ada detail."),
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
fun PlantCareCard(card: CardData, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(
                imageVector = card.icon,
                contentDescription = card.title,
                tint = Color.Black,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = card.title,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = card.description,
                fontSize = 13.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun PlantCareDialog(card: CardData, items: List<String>, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = card.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Black)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                items.forEach {
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Text("• ", fontSize = 14.sp, color = Color.Black)
                        Text(
                            it,
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TopBarMaintenance() {
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Container that groups both icon and text
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
                text = "Perawatan Jeruk",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(end = 48.dp, start = 8.dp)
            )
        }
    }
}


