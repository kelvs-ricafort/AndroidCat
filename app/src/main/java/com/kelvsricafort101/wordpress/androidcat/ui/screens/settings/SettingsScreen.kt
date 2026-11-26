package com.kelvsricafort101.wordpress.androidcat.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kelvsricafort101.wordpress.androidcat.R
import com.kelvsricafort101.wordpress.androidcat.ui.AndroidCatTopAppBar
import com.kelvsricafort101.wordpress.androidcat.ui.theme.AndroidCatTheme

@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit,
    darkMode: Boolean,
    onDarkModeChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AndroidCatTopAppBar(
                title = stringResource(R.string.nav_settings),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(R.string.settings_appearance_header),
                style = MaterialTheme.typography.titleLarge
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.settings_dark_mode_title),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(R.string.settings_dark_mode_desc),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Switch(
                    checked = darkMode,
                    onCheckedChange = onDarkModeChanged
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    AndroidCatTheme(darkTheme = false) {
        SettingsScreen(
            onNavigateUp = {},
            darkMode = false,
            onDarkModeChanged = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenDarkPreview() {
    AndroidCatTheme(darkTheme = true) {
        SettingsScreen(
            onNavigateUp = {},
            darkMode = true,
            onDarkModeChanged = {}
        )
    }
}