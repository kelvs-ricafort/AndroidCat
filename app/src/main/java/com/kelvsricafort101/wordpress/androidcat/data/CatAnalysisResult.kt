package com.kelvsricafort101.wordpress.androidcat.data

data class CatAnalysisResult(
    val isCatDetected: Boolean,
    val catConfidencePercent: Int,
    val labels: List<DetectedLabel>
)
