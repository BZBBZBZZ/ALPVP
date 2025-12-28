package com.example.alpvp.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Retrofit
// GET Questions
data class QuestionWrapper(
    val data: List<QuestionResponse>
)

data class QuestionResponse(
    val id: Int,
    val question_text: String,
    val option_a: String,
    val option_b: String,
    val option_c: String,
    val option_d: String
)

// POST Submit
data class SubmitQuizRequest(
    val answers: List<UserAnswerRequest>
)

data class UserAnswerRequest(
    val question_id: Int,
    val answer: String
)

// Response Result
@Parcelize
data class QuizResultResponse(
    val total_questions: Int,
    val correct_count: Int,
    val score: Int,
    val details: List<ResultDetail>? = null
) : Parcelable

@Parcelize
data class ResultDetail(
    val question_id: Int,
    val question_text: String? = null,
    val user_answer: String? = null,
    val correct_answer: String? = null,
    val is_correct: Boolean,
    val explanation: String? = null
) : Parcelable