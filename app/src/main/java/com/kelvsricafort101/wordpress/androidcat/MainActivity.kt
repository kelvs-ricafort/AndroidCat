package com.kelvsricafort101.wordpress.androidcat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kelvsricafort101.wordpress.androidcat.ui.AndroidCatApp
import com.kelvsricafort101.wordpress.androidcat.ui.theme.AndroidCatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidCatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AndroidCatApp()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AndroidCatAppPreview() {
    AndroidCatTheme(darkTheme = false) {
        Surface {
            AndroidCatApp()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AndroidCatAppDarkPreview() {
    AndroidCatTheme(darkTheme = true) {
        Surface {
            AndroidCatApp()
        }
    }
}