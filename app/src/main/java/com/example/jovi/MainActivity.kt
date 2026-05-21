package com.example.jovi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.jovi.navigation.JoviNavGraph
import com.example.jovi.ui.theme.JoviTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JoviTheme {
                val navController = rememberNavController()
                JoviNavGraph(navController = navController)
            }
        }
    }
}