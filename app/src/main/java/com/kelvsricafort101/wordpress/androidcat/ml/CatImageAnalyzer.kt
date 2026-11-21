package com.kelvsricafort101.wordpress.androidcat.ml

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.kelvsricafort101.wordpress.androidcat.data.CatAnalysisResult
import com.kelvsricafort101.wordpress.androidcat.data.DetectedLabel
import kotlinx.coroutines.tasks.await

class CatImageAnalyzer(
    private val context: Context
) {
    private val labeler by lazy {
        val options = ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.55f)
            .build()
        ImageLabeling.getClient(options)
    }

    suspend fun analyzeImage(imageUri: Uri): CatAnalysisResult {
        val inputImage = InputImage.fromFilePath(context, imageUri)
        val rawLabels = labeler.process(inputImage).await()

        val mappedLabels = rawLabels
            .sortedByDescending { it.confidence }
            .map { label ->
                DetectedLabel(
                    text = label.text,
                    confidencePercent = (label.confidence * 100).toInt()
                )
            }

        val catMatch = mappedLabels.firstOrNull { label ->
            label.text.equals("Cat", ignoreCase = true) ||
                    label.text.contains("Kitten", ignoreCase = true) ||
                    label.text.contains("Felidae", ignoreCase = true)
        }

        return CatAnalysisResult(
            isCatDetected = catMatch != null,
            catConfidencePercent = catMatch?.confidencePercent ?: 0,
            labels = mappedLabels
        )
    }
}