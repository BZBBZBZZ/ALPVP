package com.example.alpvp.ui.route

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// Import View
import com.example.alpvp.ui.view.DetailView
import com.example.alpvp.ui.view.HomeView
import com.example.alpvp.ui.view.QuizView
import com.example.alpvp.ui.view.ResultView // <--- PASTIKAN INI ADA (atau sesuaikan namanya)

// Import ViewModel
import com.example.alpvp.ui.viewmodel.DetailViewModel
import com.example.alpvp.ui.viewmodel.HomeViewModel
import com.example.alpvp.ui.viewmodel.QuizViewModel

@Composable
fun AppRouting() {
    val navController = rememberNavController()

    val homeViewModel: HomeViewModel = viewModel()
    val detailViewModel: DetailViewModel = viewModel()
    val quizViewModel: QuizViewModel = viewModel() // ViewModel ini dipakai bareng oleh Quiz dan Result

    NavHost(navController = navController, startDestination = "home") {

        // 1. Home
        composable("home") {
            HomeView(viewModel = homeViewModel, navController = navController)
        }

        // 2. Detail
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

        // 3. Quiz
        composable("Quiz") {
            QuizView(
                viewModel = quizViewModel,
                navController = navController
            )
        }

        // 4. Result (INI YANG TADI HILANG)
        composable("Result") {
            // Kita oper viewModel yang sama biar skornya tampil
            ResultView(
                viewModel = quizViewModel,
                navController = navController
            )
        }

    }
}