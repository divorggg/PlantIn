package com.example.plantin.classification.history

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.plantin.MainActivity
import com.example.plantin.classification.history.dao.DetectionHistory
import com.example.plantin.classification.history.dao.HistoryViewModel
import com.example.plantin.notes.NotesPage
import com.example.plantin.ui.sidebar.Sidebar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = viewModel()
) {
    val historyList by viewModel.history.collectAsState()

    // Ambil 4 entri terakhir berdasarkan timestamp terbaru
    val recentHistory = historyList
        .sortedByDescending { it.timestamp }
        .take(4)

    // Group history by date
    val groupedHistory = recentHistory.groupBy { history ->
        SimpleDateFormat("dd MMMM yyyy", Locale("id")).format(history.timestamp)
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedMenu by remember { mutableStateOf("Hasil Scan") }
    val context = LocalContext.current

    val onMenuNavigated: () -> Unit = {
        selectedMenu = "Hasil Scan"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Sidebar(
                selectedMenu = selectedMenu,
                onNavigate = { menu ->
                    if (selectedMenu != menu) {
                        selectedMenu = menu
                        scope.launch { drawerState.close() }

                        when (menu) {
                            "Beranda" -> {
                                context.startActivity(Intent(context, MainActivity::class.java))
                                onMenuNavigated()
                            }

                            "Catatan" -> {
                                context.startActivity(Intent(context, NotesPage::class.java))
                                onMenuNavigated()
                            }

                            "Hasil Scan" -> {
                                val intent = Intent(context, HistoryActivity::class.java)
                                context.startActivity(intent)
                                onMenuNavigated()
                            }
                        }
                    } else {
                        scope.launch { drawerState.close() }
                    }
                }
            )
        }
    ) {
        Scaffold(modifier = Modifier
            .windowInsetsPadding(WindowInsets.statusBars)
            .fillMaxSize(),
            topBar = {
                TopBarScan(
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
        ) { paddingValues ->
            if (groupedHistory.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tidak ada riwayat scan",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            } else
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    groupedHistory.forEach { (date, historyItems) ->
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Text(
                                text = date,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Thin,
                                color = Color.Black,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }

                        items(historyItems) { item ->
                            HistoryItem(history = item)
                        }
                    }
                }
        }
    }
}

@Composable
fun HistoryItem(history: DetectionHistory) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        color = Color.White,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
        ) {
            // Background image
            Image(
                painter = rememberAsyncImagePainter(Uri.parse(history.imageUri)),
                contentDescription = "Gambar hasil deteksi: ${history.label.ifBlank { "Tidak Terdeteksi" }}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Overlay gradient for better text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.Black.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(16.dp)
                    )
            )

            // Timestamp badge - top right
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = SimpleDateFormat(
                        "HH.mm",
                        Locale("id")
                    ).format(history.timestamp) + " WIB",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }

            // Label badge - bottom

            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()

                    .padding(vertical = 12.dp, horizontal = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.6f) // Membuat background transparan
                ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)

                ) {
                    Text(
                        text = history.label.ifBlank { "Tidak Terdeteksi" },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (history.label.isBlank()) Color.Gray else Color.Black,
                        maxLines = 2
                    )
                }
            }

        }
    }
}

@Composable
fun TopBarScan(onMenuClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
                )
                .clip(RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp))
                .background(Color(0xFFFAD7BE))
                .padding(horizontal = 16.dp, vertical = 2.dp)
        ) {
            IconButton(
                onClick = onMenuClick, modifier = Modifier
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
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.Black,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }


            }
            Text(
                text = "Hasil Scan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(end = 48.dp, start = 8.dp)
            )
        }
    }
}
