package com.kelvsricafort101.wordpress.androidcat.screens

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(
    name = "settings"
)
class SettingsDataStore(
    private val context: Context
) {
    private companion object {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
    }

    val settingsFlow: Flow<SettingsUiState> = context.settingsDataStore.data.map { preferences ->
        SettingsUiState(
            darkMode = preferences[DARK_MODE] ?: false
        )
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[DARK_MODE] = enabled
        }
    }
}