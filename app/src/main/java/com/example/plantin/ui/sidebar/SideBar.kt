package com.example.plantin.ui.sidebar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantin.R

@Composable
fun Sidebar(selectedMenu: String, onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(250.dp)
            .background(Color(0xFF054D3B))

    ) {
        // Header dengan logo dan teks "Halo, User!"
        Row(
            modifier = Modifier
                .fillMaxWidth() // Pastikan header penuh
                .padding(16.dp)
                .statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically // Menengahkan elemen secara vertikal
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_plantin),
                contentDescription = "Logo",
                modifier = Modifier
                    .clip(CircleShape) // Membuat gambar menjadi bulat
                    .size(60.dp)
                    .border(2.dp, Color.White, CircleShape)

            )
            Spacer(modifier = Modifier.width(8.dp)) // Gunakan width agar ada jarak horizontal

            Text(
                text = "Halo, User!",
                fontSize = 18.sp,
                color = Color.White,

                )
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            SidebarMenuItem(
                "Beranda",
                R.drawable.icon_home,
                selectedMenu == "Beranda"
            ) { onNavigate("Beranda") }
            SidebarMenuItem(
                "Catatan",
                R.drawable.icon_report,
                selectedMenu == "Catatan"
            ) { onNavigate("Catatan") }
            SidebarMenuItem(
                "Hasil Scan",
                R.drawable.icon_gallery,
                selectedMenu == "Hasil Scan"
            ) { onNavigate("Hasil Scan") }
        }
    }
}

