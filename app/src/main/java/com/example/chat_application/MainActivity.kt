package com.example.chat_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.chat_application.presentation.RootGraph
import com.example.chat_application.ui.theme.Chat_ApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Chat_ApplicationTheme {
                val navController = rememberNavController()
                RootGraph(navController = navController)
            }
        }
    }
}

