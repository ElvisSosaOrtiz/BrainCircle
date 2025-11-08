package com.example.braincircle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.braincircle.ui.theme.BrainCircleTheme
import com.example.braincircle.view.BrainCircleApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var keepOn = true
        splashScreen.setKeepOnScreenCondition { keepOn }
        setContent {
            BrainCircleTheme {
                BrainCircleApp()
            }
        }
        lifecycleScope.launch {
            delay(700)
            keepOn = false
        }
    }
}