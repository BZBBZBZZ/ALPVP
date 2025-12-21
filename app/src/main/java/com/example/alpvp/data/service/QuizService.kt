package com.example.alpvp.data.service

import com.example.alpvp.ui.model.QuestionWrapper
import com.example.alpvp.ui.model.QuizResultResponse
import com.example.alpvp.ui.model.SubmitQuizRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface QuizService {
    @GET("api/quiz/start")
    suspend fun getQuestions(): Response<QuestionWrapper>

    @POST("api/quiz/submit")
    suspend fun submitQuiz(@Body request: SubmitQuizRequest): Response<QuizResultResponse>
}