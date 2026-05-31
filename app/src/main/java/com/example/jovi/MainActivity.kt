package com.example.jovi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import coil.Coil
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.example.jovi.data.db.JoviDatabase
import com.example.jovi.navigation.JoviNavGraph
import com.example.jovi.ui.theme.JoviTheme
import com.example.jovi.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModel.Factory((application as JoviApplication).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Coil.setImageLoader(ImageLoader.Builder(this).components { add(SvgDecoder.Factory()) }.build())
        JoviDatabase.getInstance(applicationContext)
        enableEdgeToEdge()
        setContent {
            val settings by settingsViewModel.settings.collectAsState()
            val darkMode = settings?.darkMode ?: false
            JoviTheme(darkTheme = darkMode) {
                val navController = rememberNavController()
                JoviNavGraph(navController = navController, settingsViewModel = settingsViewModel)
            }
        }
    }
}
