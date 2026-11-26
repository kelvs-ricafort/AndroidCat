package com.kelvsricafort101.wordpress.androidcat.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kelvsricafort101.wordpress.androidcat.R
import com.kelvsricafort101.wordpress.androidcat.ml.CatImageAnalyzer
import com.kelvsricafort101.wordpress.androidcat.navigation.AndroidCatNavHost
import com.kelvsricafort101.wordpress.androidcat.ui.screens.settings.SettingsDataStore
import com.kelvsricafort101.wordpress.androidcat.ui.screens.settings.SettingsViewModel
import com.kelvsricafort101.wordpress.androidcat.ui.screens.settings.SettingsViewModelFactory
import com.kelvsricafort101.wordpress.androidcat.ui.theme.AndroidCatTheme

/**
 * Top level composable that represents screens for the application.
 */
@Composable
fun AndroidCatApp(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val settingsDataStore = remember { SettingsDataStore(context = context.applicationContext) }
    val imageAnalyzer = remember { CatImageAnalyzer(context) }
    val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModelFactory(settingsDataStore = settingsDataStore, imageAnalyzer = imageAnalyzer))
    val settingsUiState by settingsViewModel.uiState.collectAsState()
    val scannerUiState by settingsViewModel.scannerUiState.collectAsState()

    AndroidCatTheme(
        darkTheme = settingsUiState.darkMode
    ) {
        AndroidCatNavHost(
            navController = navController,
            settingsUiState = settingsUiState,
            onDarkModeChanged = settingsViewModel::setDarkMode,
            scannerUiState = scannerUiState,
            onImageSelected = settingsViewModel::onImageSelected
        )
    }
}

/**
 * App bar to display title and conditionally display the back navigation.
 */
@Composable
fun AndroidCatTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(
                    onClick = navigateUp
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            } else {
                navigationIcon()
            }
        },
        actions = actions
    )
}