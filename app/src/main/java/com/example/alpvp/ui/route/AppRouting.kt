package com.example.alpvp.ui.route

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.alpvp.data.factory.ViewModelFactory
import com.example.alpvp.data.container.AppContainer
import com.example.alpvp.ui.view.*
import com.example.alpvp.ui.viewmodel.*

@Composable
fun AppRouting() {
    val navController = rememberNavController()
    val factory = ViewModelFactory(AppContainer())

    // Inisialisasi ViewModel dengan factory untuk menyediakan dependensi
    val authViewModel: AuthViewModel = viewModel(factory = factory)
    val leaderboardViewModel: LeaderboardViewModel = viewModel(factory = factory)

    // ViewModel tanpa dependensi dapat diinisialisasi seperti biasa
    val homeViewModel: HomeViewModel = viewModel()
    val detailViewModel: DetailViewModel = viewModel()
    val quizViewModel: QuizViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginView(navController = navController, viewModel = authViewModel)
        }

        composable("register") {
            RegisterView(navController = navController, viewModel = authViewModel)
        }

        // Rute HOME (Dashboard) dengan ViewModel yang diperlukan
        composable("home") {
            DashboardView(
                navController = navController,
                authViewModel = authViewModel,
                leaderboardViewModel = leaderboardViewModel
            )
        }

        // Rute Materi
        composable("materi") {
            HomeView(viewModel = homeViewModel, navController = navController)
        }

        // Rute Detail
        composable(
            route = "detail/{foodId}",
            arguments = listOf(navArgument("foodId") { type = NavType.IntType })
        ) { backStackEntry ->
            val foodId = backStackEntry.arguments?.getInt("foodId") ?: 0
            DetailView(
                foodId = foodId,
                viewModel = detailViewModel,
                navController = navController
            )
        }

        // Rute Quiz
        composable("Quiz") {
            QuizView(viewModel = quizViewModel, navController = navController)
        }

        // Rute Result
        composable("Result") {
            ResultView(viewModel = quizViewModel, navController = navController)
        }

        // Rute Leaderboard dengan ViewModel
        composable("leaderboard") {
            LeaderboardView(navController = navController, viewModel = leaderboardViewModel)
        }
    }
}