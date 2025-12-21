package com.example.alpvp.ui.uistate

import com.example.alpvp.ui.model.QuestionResponse
import com.example.alpvp.ui.model.QuizResultResponse

// state buat halaman Quiz, pas main
sealed interface QuizUiState {
    object Loading : QuizUiState
    data class Success(
        val questions: List<QuestionResponse>,
        val currentQuestionIndex: Int = 0,
        val timeLeft: Int = 10,
        val userAnswers: MutableMap<Int, String> = mutableMapOf()
    ) : QuizUiState
    data class Error(val message: String) : QuizUiState
}

// state buat halaman Result
sealed interface ResultUiState {
    object Loading : ResultUiState
    data class Success(val result: QuizResultResponse) : ResultUiState
    data class Error(val message: String) : ResultUiState
}