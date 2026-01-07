package com.example.alpvp.ui.route

import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.alpvp.data.container.DefaultAppContainer
import com.example.alpvp.ui.view.HomeScreen
import com.example.alpvp.ui.view.LoginScreen
import com.example.alpvp.ui.view.RegisterScreen
import com.example.alpvp.ui.view.LeaderboardScreen
import com.example.alpvp.ui.viewmodel.LeaderboardViewModel
import com.example.alpvp.ui.viewmodel.ViewModelFactory

private const val TAG = "NavigationSetup"

/**
 * Function untuk setup semua navigation routes
 */
fun setupNavGraph(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.apply {
        // Rute untuk Login
        composable(AppRoutes.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    Log.d(TAG, "Login successful, navigating to Home")
                    navController.navigate(AppRoutes.Home.route) {
                        popUpTo(AppRoutes.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    Log.d(TAG, "Navigating to Register")
                    navController.navigate(AppRoutes.Register.route)
                }
            )
        }

        // Rute untuk Register
        composable(AppRoutes.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    Log.d(TAG, "Register successful, navigating back")
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    Log.d(TAG, "Navigating back to Login")
                    navController.popBackStack()
                }
            )
        }

        // Rute untuk Home
        composable(AppRoutes.Home.route) {
            Log.d(TAG, "Rendering HomeScreen")
            HomeScreen(
                onNavigateToLeaderboard = {
                    Log.d(TAG, "Navigating to Leaderboard from Home")
                    try {
                        navController.navigate(AppRoutes.Leaderboard.route)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error navigating to Leaderboard: ${e.message}", e)
                    }
                }
            )
        }

        // Rute untuk Leaderboard
        composable(AppRoutes.Leaderboard.route) {
            Log.d(TAG, "Rendering LeaderboardScreen")
            // Create ViewModel in the composable scope
            val leaderboardViewModel: LeaderboardViewModel = viewModel(
                factory = ViewModelFactory(DefaultAppContainer())
            )
            LeaderboardScreen(
                onNavigateToHome = {
                    Log.d(TAG, "Navigating back to Home from Leaderboard")
                    try {
                        navController.navigate(AppRoutes.Home.route) {
                            popUpTo(AppRoutes.Home.route) { inclusive = false }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error navigating to Home: ${e.message}", e)
                    }
                },
                leaderboardViewModel = leaderboardViewModel
            )
        }
    }
}



