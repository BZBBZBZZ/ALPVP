package com.example.alpvp.ui.route

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.alpvp.ui.view.*
import com.example.alpvp.ui.viewmodel.*

@Composable
fun AppRouting() {
    val navController = rememberNavController()

    val homeViewModel: HomeViewModel = viewModel()
    val detailViewModel: DetailViewModel = viewModel()
    val quizViewModel: QuizViewModel = viewModel()

    // startDestination = "home" artinya aplikasi mulai di DashboardView
    NavHost(navController = navController, startDestination = "home") {

        // 1. ROUTE HOME (BERANDA) -> TAMPILKAN DASHBOARD VIEW
        composable("home") {
            DashboardView(navController = navController)
        }

        // 2. ROUTE MATERI -> TAMPILKAN FILE HOMEVIEW (GRID MAKANAN)
        composable("materi") {
            HomeView(viewModel = homeViewModel, navController = navController)
        }

        // 3. ROUTE DETAIL
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

        // 4. ROUTE QUIZ
        composable("Quiz") {
            QuizView(viewModel = quizViewModel, navController = navController)
        }

        // 5. ROUTE RESULT
        composable("Result") {
            ResultView(viewModel = quizViewModel, navController = navController)
        }

        // 6. ROUTE LEADERBOARD
        composable("leaderboard") {
            LeaderboardView(navController = navController)
        }
    }
}