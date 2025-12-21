package com.example.alpvp.ui.route

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alpvp.ui.view.QuizView
import com.example.alpvp.ui.view.ResultView
import com.example.alpvp.ui.viewmodel.QuizViewModel

@Composable
fun AppRouting() {
    val navController = rememberNavController()
    val quizViewModel: QuizViewModel = viewModel()

    NavHost(navController = navController, startDestination = "Quiz") {

        composable("Quiz") {
            QuizView(viewModel = quizViewModel, navController = navController)
        }

        composable("Result") {
            ResultView(viewModel = quizViewModel, navController = navController)
        }

    }
}