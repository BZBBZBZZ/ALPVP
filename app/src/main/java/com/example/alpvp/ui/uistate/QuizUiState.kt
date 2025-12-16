package com.example.alpvp.ui.uistate

import com.example.alpvp.ui.model.QuestionResponse
import com.example.alpvp.ui.model.QuizResultResponse

// State untuk halaman Quiz (Sedang main)
sealed interface QuizUiState {
    object Loading : QuizUiState
    data class Success(
        val questions: List<QuestionResponse>,
        val currentQuestionIndex: Int = 0,
        val timeLeft: Int = 10, // Timer 10 detik
        val userAnswers: MutableMap<Int, String> = mutableMapOf()
    ) : QuizUiState
    data class Error(val message: String) : QuizUiState
}

// State untuk halaman Result (Hasil)
sealed interface ResultUiState {
    object Loading : ResultUiState
    data class Success(val result: QuizResultResponse) : ResultUiState
    data class Error(val message: String) : ResultUiState
}