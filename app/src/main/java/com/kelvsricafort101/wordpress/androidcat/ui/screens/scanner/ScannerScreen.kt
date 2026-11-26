package com.kelvsricafort101.wordpress.androidcat.ui.screens.scanner

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.kelvsricafort101.wordpress.androidcat.R
import com.kelvsricafort101.wordpress.androidcat.data.CatAnalysisResult
import com.kelvsricafort101.wordpress.androidcat.data.DetectedLabel
import com.kelvsricafort101.wordpress.androidcat.ui.screens.settings.ScannerUiState
import com.kelvsricafort101.wordpress.androidcat.ui.theme.AndroidCatTheme
import java.io.File

@Composable
fun ScannerScreen(
    uiState: ScannerUiState,
    onImageSelected: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onImageSelected(uri)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val captured = tempCameraUri
        if (success && captured != null) {
            onImageSelected(captured)
        }
    }

    ScannerScreenContent(
        uiState = uiState,
        onPickFromGallery = {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onCapturePhoto = {
            val uri = createTempImageUri(context)
            tempCameraUri = uri
            cameraLauncher.launch(uri)
        },
        modifier = modifier
    )
}

@Composable
fun ScannerScreenContent(
    uiState: ScannerUiState,
    onPickFromGallery: () -> Unit,
    onCapturePhoto: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.scanner_headline),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.scanner_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Image Preview Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
            )
        ) {
            val imageUri = when(uiState) {
                is ScannerUiState.Idle -> null
                is ScannerUiState.Analyzing -> uiState.imageUri
                is ScannerUiState.Success -> uiState.imageUri
                is ScannerUiState.Error -> uiState.imageUri
            }

            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = stringResource(R.string.cd_selected_photo),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp))
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Pets,
                            contentDescription = stringResource(R.string.cd_cat_placeholder),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.btn_pick_gallery),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onPickFromGallery,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Rounded.PhotoLibrary,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.btn_pick_gallery)
                )
            }
            FilledTonalButton(
                onClick = onCapturePhoto,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Rounded.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.btn_take_photo)
                )
            }
        }

        // State Output
        when (uiState) {
            is ScannerUiState.Idle -> Unit
            is ScannerUiState.Analyzing -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.analyzing_image),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            is ScannerUiState.Error -> {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.error_analysis_failed),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            is ScannerUiState.Success -> {
                val result = uiState.result
                val bannerColor = if (result.isCatDetected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.secondaryContainer
                }
                val bannerContentColor = if (result.isCatDetected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSecondaryContainer
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = bannerColor),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (result.isCatDetected) {
                                Icons.Rounded.CheckCircle
                            } else {
                                Icons.Rounded.Info
                            },
                            contentDescription = null,
                            tint = bannerContentColor,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (result.isCatDetected) {
                                    stringResource(
                                        R.string.result_cat_detected,
                                        result.catConfidencePercent
                                    )
                                } else {
                                    stringResource(R.string.result_no_cat_detected)
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = bannerContentColor
                            )
                        }
                    }
                }

                if (result.labels.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.detected_labels_header),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        result.labels.forEach { label ->
                            AssistChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = stringResource(
                                            R.string.label_confidence_format,
                                            label.text,
                                            label.confidencePercent
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun createTempImageUri(context: Context) : Uri {
    val directory = File(context.cacheDir, "camera_captures").apply { mkdirs() }
    val file = File.createTempFile("cat_capture", "jpg", directory)
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}

@Preview(showBackground = true)
@Composable
private fun ScannerScreenPreview() {
    AndroidCatTheme(darkTheme = false) {
        ScannerScreenContent(
            uiState = ScannerUiState.Success(
                imageUri = Uri.EMPTY,
                result = CatAnalysisResult(
                    isCatDetected = true,
                    catConfidencePercent = 96,
                    labels = listOf(
                        DetectedLabel("Cat", 96),
                        DetectedLabel("Whiskers", 88),
                        DetectedLabel("Pet", 84)
                    )
                )
            ),
            onPickFromGallery = {},
            onCapturePhoto = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScannerScreenDarkPreview() {
    AndroidCatTheme(darkTheme = true) {
        ScannerScreenContent(
            uiState = ScannerUiState.Success(
                imageUri = Uri.EMPTY,
                result = CatAnalysisResult(
                    isCatDetected = true,
                    catConfidencePercent = 96,
                    labels = listOf(
                        DetectedLabel("Cat", 96),
                        DetectedLabel("Whiskers", 88),
                        DetectedLabel("Pet", 84)
                    )
                )
            ),
            onPickFromGallery = {},
            onCapturePhoto = {}
        )
    }
}