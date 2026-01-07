package com.example.alpvp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alpvp.ui.theme.ALPVPTheme
import com.example.alpvp.ui.view.HomeScreen
import com.example.alpvp.ui.view.LoginScreen
import com.example.alpvp.ui.view.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ALPVPTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        // Rute untuk Login
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    // Jika login berhasil, pindah ke Home dan hapus history login agar tidak bisa back
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        // Rute untuk Register
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    // Setelah daftar, bisa langsung masuk ke home atau balik ke login
                    // Di sini kita arahkan login ulang (atau bisa langsung ke home)
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Rute untuk Home
        composable("home") {
            HomeScreen()
        }
    }
}