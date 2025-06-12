package com.example.plantin.ui.Treatment

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantin.R

@Composable
fun CitrusDiseaseScreen(onDiseaseClick: (DiseaseItem) -> Unit) {
    val diseases = listOf(
        DiseaseItem(
            title = "Black Spot",
            imageRes = R.drawable.img_blackspot,
            backgroundColor = Color(0xFFFAA96C),
            treatment = listOf(


                "Pemangkasan Sanitasi: Pangkas semua bagian tanaman yang menunjukkan gejala infeksi (daun berbercak hitam, ranting yang sakit). Gunakan alat pangkas yang steril dan bersihkan dengan alkohol 70% setiap kali berpindah tanaman.",
                "Aplikasi Fungisida Tembaga: Semprotkan fungisida berbahan dasar tembaga seperti copper sulfate atau copper oxychloride dengan konsentrasi 2-3 gram per liter air. Aplikasikan setiap 10-14 hari, terutama pada musim hujan.",
                "Pengaturan Jarak Tanam: Atur jarak antar tanaman agar sirkulasi udara optimal. Jarak ideal untuk jeruk adalah 4-6 meter antar tanaman untuk mengurangi kelembaban dan meningkatkan penetrasi sinar matahari.",
                "Penyemprotan Preventif: Lakukan penyemprotan preventif dengan larutan baking soda (5 gram per liter air) ditambah detergen cair (1 tetes) sebagai perata setiap 2 minggu sebagai tindakan pencegahan tambahan."
            )
        ),
        DiseaseItem(
            title = "Citrus Cancer",
            imageRes = R.drawable.img_citrus_canker,
            backgroundColor = Color(0xFFFAA96C),
            treatment = listOf(
                "Karantina Ketat: Segera karantina tanaman yang terinfeksi dan area sekitarnya dalam radius minimal 5 meter. Pasang pagar atau pembatas untuk mencegah akses orang dan hewan yang dapat menyebarkan bakteri.",
                "Eradikasi Total Bagian Terinfeksi: Potong dan musnahkan semua bagian tanaman yang menunjukkan gejala (daun berlubang, batang berkoreng, buah berbercak). Potong minimal 30 cm dari bagian yang sakit untuk memastikan tidak ada bakteri yang tersisa.",
                "Sterilisasi Alat dan Area: Sterilisasi semua alat kerja dengan larutan klorin 10% atau alkohol 70% sebelum dan sesudah digunakan. Sterilisasi juga sepatu bot dan pakaian kerja yang digunakan di area terinfeksi.",
                "Aplikasi Bakterisida Tembaga: Semprotkan bakterisida berbahan tembaga seperti copper hydroxide atau copper sulfate dengan konsentrasi 3-4 gram per liter air. Aplikasikan setiap 7-10 hari hingga gejala hilang, kemudian lanjutkan setiap 2 minggu.",
                "Penggunaan Streptomycin: Jika tersedia dan diizinkan, gunakan antibiotik streptomycin sulfate dengan dosis 200-500 ppm (0.2-0.5 gram per liter air). Aplikasikan setiap 5-7 hari maksimal 3 kali aplikasi berturut-turut.",
                "Pengendalian Vektor: Lakukan pengendalian intensif terhadap serangga penusuk seperti kutu daun, thrips, dan leafminer yang dapat menjadi vektor bakteri. Gunakan insektisida sistemik seperti imidacloprid setiap 2-3 minggu.",
                "Manajemen Air dan Kelembaban: Hindari penyiraman dengan sprinkler yang dapat menyebarkan bakteri melalui percikan air. Gunakan sistem irigasi tetes atau penyiraman langsung ke tanah. Pastikan drainase sangat baik.",
                "Aplikasi Copper + Mancozeb: Gunakan kombinasi copper sulfate (2 gram/liter) + mancozeb (2 gram/liter) untuk efektivitas yang lebih tinggi. Aplikasikan setiap 10 hari pada kondisi cuaca lembab.",
                "Pengamatan Intensif: Lakukan pemeriksaan harian pada tanaman dalam radius 100 meter dari lokasi infeksi. Gunakan kaca pembesar untuk melihat gejala awal berupa bercak kecil dengan halo kuning.",
                "Pembatasan Aktivitas: Batasi aktivitas di kebun saat kondisi basah (pagi hari dengan embun, setelah hujan, atau saat kelembaban tinggi) karena bakteri mudah menyebar dalam kondisi lembab.",
                "Disinfeksi Kendaraan dan Equipment: Semprotkan disinfektan pada roda kendaraan, sepatu, dan peralatan yang masuk ke area terinfeksi. Gunakan larutan klorin 2% atau formalin 2%.",
                "Pelaporan ke Dinas Pertanian: Laporkan kasus ini ke dinas pertanian setempat karena citrus canker merupakan penyakit karantina yang memerlukan penanganan khusus dan koordinasi dengan pihak berwenang."
            )
        ),
        DiseaseItem(
            title = "Greening Disease",
            imageRes = R.drawable.img_greening,
            backgroundColor = Color(0xFFFAA96C),
            treatment = listOf(
                "Deteksi Dini dengan Uji Lab: Lakukan uji PCR (Polymerase Chain Reaction) untuk konfirmasi diagnosis karena gejala mirip dengan defisiensi nutrisi. Uji ini dapat mendeteksi bakteri Candidatus Liberibacter asiaticus bahkan sebelum gejala terlihat jelas.",
                "Eradikasi Tanaman Terinfeksi: Cabut dan musnahkan seluruh tanaman yang positif terinfeksi beserta akarnya. Jangan lakukan pencangkokan atau okulasi dari tanaman ini karena bakteri bersifat sistemik di seluruh jaringan tanaman.",
                "Pengendalian Vektor Psyllid: Lakukan pengendalian intensif terhadap serangga Asian Citrus Psyllid (Diaphorina citri) yang merupakan vektor utama. Gunakan insektisida sistemik seperti imidacloprid (0.5 ml/liter) setiap 2 minggu.",
                "Aplikasi Insektisida Rotasi: Rotasi penggunaan insektisida dengan bahan aktif berbeda untuk mencegah resistensi: Week 1-2: Imidacloprid, Week 3-4: Thiamethoxam, Week 5-6: Chlorpyrifos, Week 7-8: Malathion.",
                "Monitoring dengan Yellow Sticky Trap: Pasang perangkap kuning lengket (20-30 buah per hektar) untuk memantau populasi psyllid. Ganti setiap minggu dan catat jumlah psyllid yang terperangkap untuk menentukan intensitas pengendalian.",
                "Aplikasi Antibiotik Terbatas: Jika diizinkan oleh otoritas pertanian, aplikasikan oxytetracycline atau streptomycin dengan dosis 1 gram per liter air melalui injeksi batang atau aplikasi tanah. Maksimal 3 kali setahun.",
                "Peningkatan Nutrisi Tanaman: Berikan nutrisi tambahan terutama zinc, mangan, dan iron melalui aplikasi foliar setiap 2 minggu. Gunakan chelated micronutrients untuk penyerapan yang lebih baik dengan dosis 2-3 gram per liter air.",
                "Karantina Area Luas: Buat zona karantina dengan radius minimal 1 km dari lokasi tanaman terinfeksi. Larang pemindahan bibit, batang atas, atau bagian tanaman dari area ini ke lokasi lain.",
                "Replanting dengan Bibit Bersertifikat: Ganti tanaman yang dicabut dengan bibit jeruk yang bersertifikat bebas penyakit dan tahan HLB jika tersedia. Biarkan tanah 'istirahat' selama 6 bulan sebelum penanaman kembali.",
                "Aplikasi Terapi Nutrisi Intensif: Berikan kombinasi pupuk NPK (16:16:16) + trace elements melalui fertigation mingguan. Tambahkan asam amino dan growth promoter untuk meningkatkan daya tahan tanaman yang masih sehat.",
                "Sistem Early Warning: Buat sistem peringatan dini dengan pemeriksaan visual mingguan menggunakan checklist gejala: daun kuning blotchy, buah kecil asimetris, ranting mengering, dan penurunan produksi.",
                "Koordinasi Regional: Koordinasikan dengan petani sekitar dalam radius 5 km untuk pengendalian terpadu karena psyllid bisa terbang cukup jauh. Buat jadwal penyemprotan serentak untuk efektivitas maksimal.",
                "Pemusnahan Inang Alternatif: Bersihkan semua tanaman keluarga Rutaceae liar (seperti kemuning, jeruk purut liar) dalam radius 500 meter yang dapat menjadi inang alternatif bakteri dan psyllid.",
                "Dokumentasi dan Pelaporan: Dokumentasikan semua tindakan pengendalian, hasil uji lab, dan perkembangan penyakit. Laporkan ke Balai Proteksi Tanaman setempat untuk koordinasi penanganan skala regional."
            )
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 70.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(diseases.size) { index ->
                DiseaseCard(
                    disease = diseases[index],
                    onClick = { onDiseaseClick(diseases[index]) }
                )
            }
        }
    }
}

@Composable
fun DiseaseCard(disease: DiseaseItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = disease.backgroundColor,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = disease.title,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF0F0F0),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = disease.imageRes),
                        contentDescription = disease.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}