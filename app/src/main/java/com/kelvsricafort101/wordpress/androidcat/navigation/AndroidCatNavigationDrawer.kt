package com.kelvsricafort101.wordpress.androidcat.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kelvsricafort101.wordpress.androidcat.R
import com.kelvsricafort101.wordpress.androidcat.ui.screens.about.AboutScreenDestination
import com.kelvsricafort101.wordpress.androidcat.ui.screens.scanner.ScannerDestination
import com.kelvsricafort101.wordpress.androidcat.ui.screens.settings.SettingsDestination
import kotlinx.coroutines.launch

@Composable
fun AndroidCatNavigationDrawer(
    selectedRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (openDrawer: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(24.dp)
                )
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = stringResource(R.string.nav_scanner)
                        )
                    },
                    selected = selectedRoute == ScannerDestination.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        onNavigate(ScannerDestination.route)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Scanner,
                            contentDescription = stringResource(R.string.nav_scanner)
                        )
                    }
                )

                NavigationDrawerItem(
                    label = {
                        Text(
                            text = stringResource(R.string.nav_about)
                        )
                    },
                    selected = selectedRoute == AboutScreenDestination.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        onNavigate(AboutScreenDestination.route)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(R.string.nav_about)
                        )
                    }
                )

                NavigationDrawerItem(
                    label = {
                        Text(
                            text = stringResource(R.string.nav_settings)
                        )
                    },
                    selected = selectedRoute == SettingsDestination.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        onNavigate(SettingsDestination.route)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.nav_settings)
                        )
                    }
                )
            }
        }
    ) {
        content {
            scope.launch {
                drawerState.open()
            }
        }
    }
}