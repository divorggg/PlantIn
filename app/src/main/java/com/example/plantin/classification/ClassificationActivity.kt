package com.example.plantin.classification

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.plantin.MainActivity
import com.example.plantin.R
import com.example.plantin.ui.theme.PlantInTheme
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.UUID


class ClassificationActivity : ComponentActivity() {
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var cropImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>


    private var cameraImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Daftar launcher untuk permission CAMERA
        requestCameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    openCamera()
                } else {
                    Toast.makeText(
                        this,
                        "Izin kamera diperlukan untuk mengambil gambar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        // Launcher untuk mengambil gambar dengan kamera
        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    cameraImageUri?.let { uri ->
                        startCrop(uri)
                    } ?: run {
                        Toast.makeText(
                            this,
                            "Gagal mengambil gambar: URI kosong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(this, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show()
                }
            }


        // Launcher untuk crop gambar
        cropImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val resultUri = UCrop.getOutput(result.data!!)
                    resultUri?.let {
                        val intent = Intent(this, AnalyzeActivity::class.java)
                        intent.putExtra("croppedImageUri", it.toString())
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this, "Crop dibatalkan", Toast.LENGTH_SHORT).show()
                }
            }

        // Launcher untuk pick image dari galeri
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val selectedImageUri = result.data?.data
                    selectedImageUri?.let { uri -> startCrop(uri) }
                } else {
                    Toast.makeText(this, "Tidak jadi memilih gambar", Toast.LENGTH_SHORT).show()
                }
            }

        setContent {
            PlantInTheme {
                AwalIdentifikasiScreen(
                    onClose = {
                        val intent = Intent(this@ClassificationActivity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                        finish()
                    },
                    onTakePhotoClick = {
                        checkCameraPermissionAndOpen()
                    },
                    onUploadPhotoClick = {
                        openGallery()

                    }
                )
            }
        }
    }

    private fun checkCameraPermissionAndOpen() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }

            shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
                Toast.makeText(
                    this,
                    "Izin kamera diperlukan untuk mengambil gambar",
                    Toast.LENGTH_SHORT
                ).show()
                requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }

            else -> {
                requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        val photoFile = File.createTempFile("camera_image", ".jpg", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        cameraImageUri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.provider",
            photoFile
        )
        val uri = cameraImageUri
        if (uri != null) {
            takePictureLauncher.launch(uri)
        } else {
            Toast.makeText(this, "Gagal membuat URI untuk foto", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                type = "image/*" // Hanya izinkan gambar
            }
        pickImageLauncher.launch(intent)
    }


    private fun startCrop(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "${UUID.randomUUID()}.jpg"))

        val intent = UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(224, 224)
            .getIntent(this)

        cropImageLauncher.launch(intent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AwalIdentifikasiScreen(
    onClose: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onUploadPhotoClick: () -> Unit
) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Image(
                painter = painterResource(id = R.drawable.img_classification),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp)) // Ujung rounded
                    .background(Color.LightGray)     // Opsional, untuk latar belakang
            )
            Button(
                onClick = onTakePhotoClick,
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
                Text("Ambil Gambar")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onUploadPhotoClick,
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
                Text("Unggah Gambar")
            }
        }
    }
}



