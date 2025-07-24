package com.example.plantin.classification.result

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.example.plantin.MainActivity
import com.example.plantin.classification.history.dao.DetectionHistory
import com.example.plantin.notes.MainApplication
import com.example.plantin.ui.theme.PlantInTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.exp

class ResultActivity : ComponentActivity() {

    private var tflite: Interpreter? = null

    // Konstanta sesuai dengan model Python terbaru
    companion object {
        private const val IMAGE_SIZE = 224
        private const val NUM_CLASSES = 5
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageUriString = intent.getStringExtra("imageUri")

        setContent {
            PlantInTheme {
                if (imageUriString != null) {
                    val imageUri = imageUriString.toUri()
                    val context = LocalContext.current
                    var result by remember { mutableStateOf("Memproses...") }
                    var isLoading by remember { mutableStateOf(true) }

                    LaunchedEffect(imageUri) {
                        try {
                            withContext(Dispatchers.IO) {
                                val bitmap = uriToBitmap(imageUri, context)
                                val model = loadModelFile("model_jeruk.tflite")
                                tflite = Interpreter(model)
                                val inferenceResult = runModel(bitmap)

                                // Update state hasil
                                result = inferenceResult
                                val split = inferenceResult.split("|")
                                val label = split[0].trim()
                                val confidenceText = split.getOrNull(1)
                                    ?.replace("Confidence:", "")?.replace("%", "")?.trim()
                                val confidence = confidenceText?.toFloatOrNull() ?: 0f

                                val detectionHistory = DetectionHistory(
                                    label = label,
                                    confidence = confidence,
                                    imageUri = imageUri.toString()
                                )

                                MainApplication.database.detectionHistoryDao()
                                    .insert(detectionHistory)
                            }
                        } catch (e: Exception) {
                            result = "Error: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }

                    ResultScreen(
                        imageUri = imageUri,
                        result = result,
                        isLoading = isLoading,
                        onClose = {
                            val intent = Intent(this@ResultActivity, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                            finish()
                        }
                    )
                } else {
                    Text("Gambar tidak ditemukan.")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tflite?.close()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private suspend fun uriToBitmap(uri: Uri, context: android.content.Context): Bitmap =
        withContext(Dispatchers.IO) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }

    private suspend fun loadModelFile(modelPath: String): MappedByteBuffer =
        withContext(Dispatchers.IO) {
            val fileDescriptor = assets.openFd(modelPath)
            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength
            fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun runModel(bitmap: Bitmap): String = withContext(Dispatchers.Default) {
        val convertedBitmap = if (bitmap.config == Bitmap.Config.HARDWARE) {
            bitmap.copy(Bitmap.Config.ARGB_8888, true)
        } else {
            bitmap
        }

        val resized = Bitmap.createScaledBitmap(convertedBitmap, IMAGE_SIZE, IMAGE_SIZE, true)
        val input = Array(1) { Array(IMAGE_SIZE) { Array(IMAGE_SIZE) { FloatArray(3) } } }

        // Normalize pixel values to [0, 1] range (sesuai dengan model Python)
        for (y in 0 until IMAGE_SIZE) {
            for (x in 0 until IMAGE_SIZE) {
                val pixel = resized.getPixel(x, y)
                input[0][y][x][0] = android.graphics.Color.red(pixel) / 255.0f
                input[0][y][x][1] = android.graphics.Color.green(pixel) / 255.0f
                input[0][y][x][2] = android.graphics.Color.blue(pixel) / 255.0f
            }
        }

        // Output sesuai dengan model Python (4 classes)
        val output = Array(1) { FloatArray(NUM_CLASSES) }

        tflite?.run(input, output)

        // Labels sesuai dengan model Python terbaru
        val classNames = listOf("Black Spot", "Citrus Canker", "Fresh", "Greening", "Not Orange")

        // Apply softmax untuk mendapatkan confidence scores
        val expValues = output[0].map { exp(it.toDouble()) }
        val sumExp = expValues.sum()
        val softmaxOutput = expValues.map { (it / sumExp).toFloat() }

        val maxIdx = softmaxOutput.indices.maxByOrNull { softmaxOutput[it] } ?: -1
        val confidence = softmaxOutput[maxIdx]

        // Untuk model terbaru, langsung return hasil tanpa threshold
        // karena model sudah memiliki class "Not Orange" untuk deteksi non-orange
        return@withContext "${classNames[maxIdx]}|Confidence: ${"%.2f".format(confidence * 100)}%"
    }

    /**
     * Fungsi tambahan untuk mendapatkan probabilitas semua kelas
     */
    private fun getAllClassProbabilities(softmaxOutput: FloatArray): Map<String, Float> {
        val classNames = listOf("Black Spot", "Citrus Canker", "Fresh", "Greening", "Not Orange")
        return classNames.mapIndexed { index, name ->
            name to softmaxOutput[index]
        }.toMap()
    }

    /**
     * Fungsi untuk debugging - menampilkan semua probabilitas kelas
     */
    private fun debugProbabilities(softmaxOutput: FloatArray): String {
        val classNames = listOf("Black Spot", "Citrus Canker", "Fresh", "Greening", "Not Orange")
        val probabilities = StringBuilder()
        probabilities.append("Class probabilities:\n")

        classNames.forEachIndexed { index, className ->
            probabilities.append("  $className: ${"%.4f".format(softmaxOutput[index])}\n")
        }

        return probabilities.toString()
    }
}