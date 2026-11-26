package com.kelvsricafort101.wordpress.androidcat.ui.screens.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kelvsricafort101.wordpress.androidcat.data.CatAnalysisResult
import com.kelvsricafort101.wordpress.androidcat.ml.CatImageAnalyzer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val darkMode: Boolean = false
)

sealed interface ScannerUiState {
    data object Idle: ScannerUiState
    data class Analyzing(val imageUri: Uri): ScannerUiState
    data class Success(val imageUri: Uri, val result: CatAnalysisResult): ScannerUiState
    data class Error(val imageUri: Uri?): ScannerUiState
}

class SettingsViewModel(
    private val settingsDataStore: SettingsDataStore,
    private val imageAnalyzer: CatImageAnalyzer
): ViewModel() {
    val uiState: StateFlow<SettingsUiState> =
        settingsDataStore.settingsFlow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = SettingsUiState()
            )
    private val _scannerUiState = MutableStateFlow<ScannerUiState>(ScannerUiState.Idle)
    val scannerUiState: StateFlow<ScannerUiState> = _scannerUiState.asStateFlow()

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setDarkMode(enabled)
        }
    }

    fun onImageSelected(uri: Uri) {
        viewModelScope.launch {
            _scannerUiState.value = ScannerUiState.Analyzing(uri)
            try {
                val result = imageAnalyzer.analyzeImage(uri)
                _scannerUiState.value = ScannerUiState.Success(uri, result)
            } catch (_: Exception) {
                _scannerUiState.value = ScannerUiState.Error(uri)
            }
        }
    }
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

class SettingsViewModelFactory(
    private val settingsDataStore: SettingsDataStore,
    private val imageAnalyzer: CatImageAnalyzer
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(
                settingsDataStore = settingsDataStore,
                imageAnalyzer = imageAnalyzer
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

