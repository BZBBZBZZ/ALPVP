package com.example.alpvp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alpvp.data.repository.QuizRepository
import com.example.alpvp.ui.model.SubmitQuizRequest
import com.example.alpvp.ui.model.UserAnswerRequest
import com.example.alpvp.ui.uistate.QuizUiState
import com.example.alpvp.ui.uistate.ResultUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import kotlin.text.get

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

    var quizUiState: QuizUiState by mutableStateOf(QuizUiState.Loading)
        private set

    var resultState: ResultUiState by mutableStateOf(ResultUiState.Loading)
        private set

    var shouldNavigateToResult by mutableStateOf(false)
        private set

    private var timerJob: Job? = null

    fun loadQuestions() {
        // 1. Reset State
        timerJob?.cancel()
        shouldNavigateToResult = false
        resultState = ResultUiState.Loading
        quizUiState = QuizUiState.Loading

        viewModelScope.launch {
            try {
                // PERBAIKAN 1: Panggil method yang benar 'getAllQuestions()'
                val response = repository.getAllQuestions()

                if (response.isSuccessful && response.body() != null) {
                    // Ambil list pertanyaan dari dalam wrapper 'data'
                    val questionWrapper = response.body()!!
                    val questionList = questionWrapper.data

                    // 2. ACAK SOAL (SHUFFLE)
                    val shuffledQuestions = questionList.shuffled()

                    quizUiState = QuizUiState.Success(
                        questions = shuffledQuestions,
                        currentQuestionIndex = 0,
                        timeLeft = 10,
                        userAnswers = mutableMapOf()
                    )
                } else {
                    quizUiState = QuizUiState.Error("Gagal mengambil soal: ${response.message()}")
                }

            } catch (e: IOException) {
                quizUiState = QuizUiState.Error("Jaringan error: ${e.message}")
            } catch (e: HttpException) {
                quizUiState = QuizUiState.Error("Server error: ${e.message}")
            } catch (e: Exception) {
                quizUiState = QuizUiState.Error("Unknown error: ${e.message}")
            }
        }
    }

    fun updateTimer() {
        val currentState = quizUiState
        if (currentState is QuizUiState.Success) {
            if (currentState.timeLeft > 0) {
                quizUiState = currentState.copy(timeLeft = currentState.timeLeft - 1)
            } else {
                handleTimeout()
            }
        }
    }

    fun handleTimeout() {
        val state = quizUiState
        if (state is QuizUiState.Success) {
            // Jika waktu habis, simpan jawaban kosong
            saveAnswer(state.questions[state.currentQuestionIndex].id, "")
            nextQuestion()
        }
    }

    fun saveAnswer(questionId: Int, answer: String) {
        val currentState = quizUiState
        if (currentState is QuizUiState.Success) {
            currentState.userAnswers[questionId] = answer
        }
    }

    fun nextQuestion() {
        val currentState = quizUiState
        if (currentState is QuizUiState.Success) {
            timerJob?.cancel()
            if (currentState.currentQuestionIndex < currentState.questions.size - 1) {
                quizUiState = currentState.copy(
                    currentQuestionIndex = currentState.currentQuestionIndex + 1,
                    timeLeft = 10
                )
            } else {
                submitQuiz()
            }
        }
    }

    private fun submitQuiz() {
        val currentState = quizUiState
        if (currentState is QuizUiState.Success) {
            viewModelScope.launch {
                try {
                    val answersList = currentState.userAnswers.map {
                        UserAnswerRequest(it.key, it.value)
                    }
                    val request = SubmitQuizRequest(answersList)

                    // PERBAIKAN 2: Handle ResponseWrapper
                    val response = repository.submitQuiz(request)

                    if (response.isSuccessful && response.body() != null) {
                        // Ambil 'data' di dalam wrapper
                        val submissionWrapper = response.body()!!
                        val resultData = submissionWrapper.data

                        resultState = ResultUiState.Success(resultData)
                        shouldNavigateToResult = true
                    } else {
                        resultState = ResultUiState.Error("Gagal submit: ${response.message()}")
                        shouldNavigateToResult = true
                    }

                } catch (e: Exception) {
                    resultState = ResultUiState.Error(e.message ?: "Gagal submit")
                    shouldNavigateToResult = true
                }
            }
        }
    }

    fun resetNavigationFlag() {
        shouldNavigateToResult = false
    }

    fun answerQuestion(answer: String) {
        val currentState = quizUiState
        if (currentState is QuizUiState.Success) {
            val questionId = currentState.questions[currentState.currentQuestionIndex].id
            saveAnswer(questionId, answer)
            nextQuestion()
        }
    }

}