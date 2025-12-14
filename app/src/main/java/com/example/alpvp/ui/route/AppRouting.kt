package com.example.alpvp.ui.route

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.alpvp.ui.view.DetailView
import com.example.alpvp.ui.view.HomeView
import com.example.alpvp.ui.viewmodel.DetailViewModel
import com.example.alpvp.ui.viewmodel.HomeViewModel

@Composable
fun AppRouting() {
    val navController = rememberNavController()

    val homeViewModel: HomeViewModel = viewModel()
    val detailViewModel: DetailViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeView(viewModel = homeViewModel, navController = navController)
        }

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
    }
}