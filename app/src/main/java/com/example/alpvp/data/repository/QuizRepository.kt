package com.example.alpvp.data.repository

import com.example.alpvp.data.service.QuizService
import com.example.alpvp.ui.model.QuizResultResponse
import com.example.alpvp.ui.model.SubmitQuizRequest
import com.example.alpvp.ui.model.QuestionWrapper
import com.example.alpvp.ui.model.QuizSubmissionWrapper
import retrofit2.Response

class QuizRepository(private val service: QuizService) {

    suspend fun getAllQuestions(): Response<QuestionWrapper> {
        return service.getQuestions()
    }

    // GANTI TIPE RETURN DI SINI:
    // Dari: Response<QuizResultResponse>
    // Jadi: Response<QuizSubmissionWrapper>
    suspend fun submitQuiz(request: SubmitQuizRequest): Response<QuizSubmissionWrapper> {
        return service.submitQuiz(request)
    }
}