package com.example.jovi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import coil.Coil
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.example.jovi.data.db.JoviDatabase
import com.example.jovi.navigation.JoviNavGraph
import com.example.jovi.ui.theme.JoviTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Coil.setImageLoader(
            ImageLoader.Builder(this)
                .components { add(SvgDecoder.Factory()) }
                .build()
        )

        JoviDatabase.getInstance(applicationContext)

        enableEdgeToEdge()
        setContent {
            JoviTheme {
                val navController = rememberNavController()
                JoviNavGraph(navController = navController)
            }
        }
    }
}
