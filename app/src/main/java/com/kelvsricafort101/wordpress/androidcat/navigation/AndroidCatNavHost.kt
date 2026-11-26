package com.kelvsricafort101.wordpress.androidcat.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kelvsricafort101.wordpress.androidcat.ui.screens.scanner.ScannerDestination
import com.kelvsricafort101.wordpress.androidcat.ui.screens.scanner.ScannerScreen
import com.kelvsricafort101.wordpress.androidcat.ui.screens.settings.ScannerUiState
import com.kelvsricafort101.wordpress.androidcat.ui.screens.settings.SettingsDestination
import com.kelvsricafort101.wordpress.androidcat.ui.screens.settings.SettingsScreen
import com.kelvsricafort101.wordpress.androidcat.ui.screens.settings.SettingsUiState

/**
 * Provides Navigation Graph for the application.
 */
@Composable
fun AndroidCatNavHost(
    navController: NavHostController,
    scannerUiState: ScannerUiState,
    onImageSelected: (Uri) -> Unit,
    settingsUiState: SettingsUiState,
    onDarkModeChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    AndroidCatNavigationDrawer(
        selectedRoute = currentRoute,
        onNavigate = { route ->
            navController.navigate(route) {
                popUpTo(ScannerDestination.route) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    ) { openDrawer ->
        NavHost(
            navController = navController,
            startDestination = ScannerDestination.route,
            modifier = modifier
        ) {
            // Scanner Screen
            composable(route = ScannerDestination.route) {
                ScannerScreen(
                    openDrawer = openDrawer,
                    uiState = scannerUiState,
                    onImageSelected = onImageSelected,
                    modifier = modifier
                )
            }
            // Settings Screen
            composable(route = SettingsDestination.route) {
                SettingsScreen(
                    onNavigateUp = {
                        navController.navigateUp()
                    },
                    darkMode = settingsUiState.darkMode,
                    onDarkModeChanged = onDarkModeChanged
                )
            }
        }
    }
}