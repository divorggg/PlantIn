package com.example.plantin.notes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantin.MainActivity
import com.example.plantin.classification.history.HistoryActivity
import com.example.plantin.ui.sidebar.Sidebar
import com.example.plantin.ui.theme.PlantInTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class NotesPage : ComponentActivity() {

    private val notesViewModel: NotesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlantInTheme {
                NotePageScreen(notesViewModel)
            }
        }
    }
}

@Composable
fun NotePageScreen(viewModel: NotesViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedMenu by remember { mutableStateOf("Catatan") }
    val context = LocalContext.current

    val onMenuNavigated: () -> Unit = {
        selectedMenu = "Catatan"
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
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                                onMenuNavigated()
                            }

                            "Catatan" -> {
                                val intent = Intent(context, NotesPage::class.java)
                                context.startActivity(intent)
                                onMenuNavigated()
                            }

                            "Hasil Scan" -> {
                                val intent = Intent(context, HistoryActivity::class.java)
                                context.startActivity(intent)
                                onMenuNavigated()
                            }
                        }
                    } else {
                        // Jika menu yang diklik adalah yang sedang aktif, cukup tutup drawer
                        scope.launch { drawerState.close() }
                    }
                }

            )
        }
    ) {
        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .fillMaxSize()
        ) {
            TopBarCtt(
                onMenuClick = { scope.launch { drawerState.open() } }
            )
            ReportListPage(viewModel = viewModel)
        }
    }
}


@Composable
fun ReportListPage(viewModel: NotesViewModel) {
    val reportList by viewModel.notesList.observeAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteSuccessMessage by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var showSuccessMessage by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (reportList.isNullOrEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    text = "tidak ada catatan"
                )
            } else {
                LazyColumn {
                    itemsIndexed(reportList!!) { _, item ->
                        ReportItem(
                            item = item,
                            onDelete = {
                                viewModel.deleteReport(item.id)
                                showDeleteSuccessMessage = true
                                coroutineScope.launch {
                                    delay(2000)
                                    showDeleteSuccessMessage = false
                                }
                            }
                        )
                    }
                }
            }
        }



        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Tambah catatan",
                tint = Color.White
            )
        }

        if (showDialog) {
            ReportInputDialog(
                onDismiss = { showDialog = false },
                onSave = { title, description ->
                    viewModel.addReport(title, description)
                    showDialog = false
                    showSuccessMessage = true
                    coroutineScope.launch {
                        delay(2000)
                        showSuccessMessage = false
                    }
                }
            )
        }
        AnimatedVisibility(
            visible = showSuccessMessage,
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = Color(0xFF006C4B), // hijau tua
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Berhasil disimpan",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = showDeleteSuccessMessage,
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.RemoveCircle,
                        contentDescription = "Success",
                        tint = Color(0xFFE10000), // hijau tua
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Berhasil dihapus",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                }
            }
        }

    }
}


@Composable
fun ReportItem(item: Notes, onDelete: () -> Unit) {
    val formattedDate =
        SimpleDateFormat("EEEE, dd MMM yyyy", Locale("id", "ID")).format(item.createdAt)

    Text(
        text = formattedDate,
        fontSize = 14.sp,

        fontWeight = FontWeight.Thin,
        color = Color.Black,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 4.dp, // Sesuaikan dengan intensitas shadow di Figma
                shape = RoundedCornerShape(16.dp),
                clip = false // Penting: agar shadow tidak ter-clip oleh background
            )
            .clip(RoundedCornerShape(16.dp)) // Shape harus sama dengan shadow
            .background(Color.White)
    ) {
        Text(
            text = SimpleDateFormat("HH.mm", Locale("id", "ID")).format(item.createdAt) + " WIB",
            fontSize = 12.sp,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 8.dp) // padding untuk posisi dari pojok
                .shadow(4.dp, RoundedCornerShape(8.dp), clip = false)
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        )
        Column(modifier = Modifier.padding(16.dp)) {
            // Bagian atas: jam di kanan
            Row(modifier = Modifier.fillMaxWidth()) {

                // Judul
                Text(
                    text = item.title,
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(8.dp))


            // Deskripsi
            item.description?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    color = Color(0xFF4F4F4F),

                    )
            }
        }
        // Tombol delete di pojok kanan bawah
        IconButton(
            onClick = onDelete,

            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.DeleteOutline,
                contentDescription = "Delete",
                tint = Color.Black
            )
        }
    }
}


@Composable
fun TopBarCtt(onMenuClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Container untuk tombol menu dan teks judul
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
                        imageVector = Icons.Default.Menu, // bisa diganti dengan menu icon kalau tersedia
                        contentDescription = "Menu",
                        tint = Color.Black,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            }
            Text(
                text = "Catatan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(end = 48.dp, start = 8.dp)
            )
        }
    }
}


@Composable
fun ReportInputDialog(
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Tambah catatan baru",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)

            )
        },
        text = {
            Column {

                Spacer(modifier = Modifier.height(16.dp))

                // Text di atas field Deskripsi
                Text(
                    text = "Judul Catatan ",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Text di atas field Deskripsi
                Text(
                    text = "Deskripsi ",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    singleLine = false
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank()) {
                        onSave(title, description)
                        onDismiss()
                    }
                },
                enabled = title.isNotBlank() && description.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (title.isNotBlank() && description.isNotBlank())
                        Color(0xFF4CAF50) // Hijau
                    else
                        Color.Gray,
                    contentColor = Color.White
                )

            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("batal")
            }

        }
    )
}

