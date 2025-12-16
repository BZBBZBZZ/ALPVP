package com.example.alpvp.ui.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// =====================
// GET Questions
// =====================
data class QuestionWrapper(
    @SerializedName("data")
    val data: List<QuestionResponse>
)

@Parcelize
data class QuestionResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("question_text") val question_text: String,
    @SerializedName("option_a") val option_a: String,
    @SerializedName("option_b") val option_b: String,
    @SerializedName("option_c") val option_c: String,
    @SerializedName("option_d") val option_d: String
) : Parcelable

// =====================
// POST Submit
// =====================
data class SubmitQuizRequest(
    @SerializedName("answers") val answers: List<UserAnswerRequest>
)

data class UserAnswerRequest(
    @SerializedName("question_id") val question_id: Int,
    @SerializedName("answer") val answer: String
)

// =====================
// RESPONSE RESULT (DIPERBAIKI)
// =====================

// 1. Ini Pembungkus Luar (Kotak "data")
data class QuizSubmissionWrapper(
    @SerializedName("data")
    val data: QuizResultResponse
)

// 2. Ini Isi Dalamnya
@Parcelize
data class QuizResultResponse(
    @SerializedName("total_questions") val total_questions: Int,
    @SerializedName("correct_count") val correct_count: Int,
    @SerializedName("score") val score: Int,
    @SerializedName("details") val details: List<ResultDetail>? = null
) : Parcelable

@Parcelize
data class ResultDetail(
    @SerializedName("question_id") val question_id: Int,
    @SerializedName("user_answer") val user_answer: String? = null,
    @SerializedName("correct_answer") val correct_answer: String? = null,
    @SerializedName("is_correct") val is_correct: Boolean,
    @SerializedName("explanation") val explanation: String? = null,
    // Kita hapus question_text dari sini karena backend memang tidak mengirimnya
) : Parcelable