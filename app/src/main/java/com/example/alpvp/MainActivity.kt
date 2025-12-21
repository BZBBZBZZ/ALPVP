package com.example.alpvp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.alpvp.ui.theme.ALPVPTheme
import com.example.alpvp.ui.route.AppRouting

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ALPVPTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppRouting()

                }
            }
        }
    }
}
