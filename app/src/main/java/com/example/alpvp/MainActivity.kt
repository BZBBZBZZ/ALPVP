package com.example.alpvp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.alpvp.ui.route.AppRouting
import com.example.alpvp.ui.theme.ALPVPTheme // Sesuaikan nama theme kamu

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ALPVPTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppRouting()
                }
            }
        }
    }
}