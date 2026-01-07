package com.example.alpvp.ui.route

/**
 * Sealed class untuk mendefinisikan semua route yang ada di aplikasi
 */
sealed class AppRoutes(val route: String) {
    object Login : AppRoutes("login")
    object Register : AppRoutes("register")
    object Home : AppRoutes("home")
    object Leaderboard : AppRoutes("leaderboard")
}

