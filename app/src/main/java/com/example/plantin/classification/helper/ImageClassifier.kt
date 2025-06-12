package com.example.plantin.classification.helper

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.exp

class ImageClassifier(context: Context) {
    private var interpreter: Interpreter
    private val labels = listOf("blackspot", "canker", "fresh", "greening")
    private val formattedLabels = listOf("Black Spot", "Canker", "Fresh", "Greening")

    init {
        val model = loadModelFile(context, "model_unquant.tflite")
        interpreter = Interpreter(model)
    }

    private fun loadModelFile(context: Context, modelPath: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun classify(image: Bitmap): Pair<String, Float> {
        // Handle hardware bitmap configuration
        val convertedBitmap = if (image.config == Bitmap.Config.HARDWARE) {
            image.copy(Bitmap.Config.ARGB_8888, true)
        } else {
            image
        }

        val resized = Bitmap.createScaledBitmap(convertedBitmap, 224, 224, true)
        val byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(224 * 224)
        resized.getPixels(intValues, 0, 224, 0, 0, 224, 224)

        // Normalize pixel values to [0, 1] range
        for (pixel in intValues) {
            val r = ((pixel shr 16) and 0xFF) / 255.0f
            val g = ((pixel shr 8) and 0xFF) / 255.0f
            val b = (pixel and 0xFF) / 255.0f
            byteBuffer.putFloat(r)
            byteBuffer.putFloat(g)
            byteBuffer.putFloat(b)
        }

        val output = Array(1) { FloatArray(4) }
        interpreter.run(byteBuffer, output)

        val confidences = output[0]

        // Apply softmax to get proper probability scores
        val expValues = confidences.map { exp(it.toDouble()) }
        val sumExp = expValues.sum()
        val softmaxOutput = expValues.map { (it / sumExp).toFloat() }.toFloatArray()

        val maxIdx = softmaxOutput.indices.maxByOrNull { softmaxOutput[it] } ?: -1
        val confidence = softmaxOutput[maxIdx]

        return formattedLabels[maxIdx] to confidence
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun classifyWithDetails(image: Bitmap): String {
        val (label, confidence) = classify(image)
        val confidencePercentage = confidence * 100
        return "$label|Confidence: ${"%.2f".format(confidencePercentage)}%"
    }

    fun close() {
        interpreter.close()
    }
}