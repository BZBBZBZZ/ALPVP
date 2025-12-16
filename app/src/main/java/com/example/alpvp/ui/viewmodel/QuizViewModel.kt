package com.example.alpvp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alpvp.data.container.AppContainer
import com.example.alpvp.ui.model.SubmitQuizRequest
import com.example.alpvp.ui.model.UserAnswerRequest
import com.example.alpvp.ui.uistate.QuizUiState
import com.example.alpvp.ui.uistate.ResultUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {
    // Inisialisasi Repository
    private val repository = AppContainer().quizRepository

    // State Halaman Main
    var quizState: QuizUiState by mutableStateOf(QuizUiState.Loading)
        private set

    // State Halaman Result
    var resultState: ResultUiState by mutableStateOf(ResultUiState.Loading)
        private set

    var hasNavigatedToResult = false

    fun resetNavigationFlag() {
        hasNavigatedToResult = false
    }

    fun setNavigationFlag() {
        hasNavigatedToResult = true
    }


    private var timerJob: Job? = null

    init {
        loadQuestions()
    }

    fun loadQuestions() {
        viewModelScope.launch {
            quizState = QuizUiState.Loading
            try {
                val response = repository.getAllQuestions()
                if (response.isSuccessful && response.body() != null) {
                    val questions = response.body()!!.data
                    quizState = QuizUiState.Success(questions = questions)
                    startTimer()
                } else {
                    val errorMsg = "Gagal: ${response.code()} - ${response.message()}"
                    quizState = QuizUiState.Error(errorMsg)

                }
            } catch (e: Exception) {
                quizState = QuizUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true)
            {
                delay(1000L)

                val currentState = quizState

                if (currentState is QuizUiState.Success) {
                    if (currentState.timeLeft > 0) {
                        quizState = currentState.copy(timeLeft = currentState.timeLeft - 1)
                    } else {
                        // Waktu habis, otomatis lanjut/salah
                        answerQuestion("") // Kirim jawaban kosong/salah
                    }
                } else {
                    break
                }
            }
        }
    }

    fun answerQuestion(answer: String) {
        val currentState = quizState
        if (currentState is QuizUiState.Success) {
            // 1. Simpan jawaban
            val currentQ = currentState.questions[currentState.currentQuestionIndex]
            currentState.userAnswers[currentQ.id] = answer

            // 2. Cek apakah masih ada soal berikutnya
            if (currentState.currentQuestionIndex < currentState.questions.size - 1) {
                // Lanjut soal berikutnya, reset timer
                quizState = currentState.copy(
                    currentQuestionIndex = currentState.currentQuestionIndex + 1,
                    timeLeft = 10 // Reset timer ke 10 detik
                )
                startTimer()
                // Timer akan otomatis lanjut karena masih dalam state Success
            } else {
                // Soal habis, Submit ke server
                timerJob?.cancel()
                // Set state ke Loading agar timer loop berhenti
                resultState = ResultUiState.Loading
                submitQuiz(currentState)
            }
        }
    }

    private fun submitQuiz(finalState: QuizUiState.Success) {
        viewModelScope.launch {
            // Pindah ke UI Result Loading dulu
            // (Note: logic navigasi nanti diatur di View berdasarkan state result)

            // Konversi jawaban map ke list request
            val answerList = finalState.userAnswers.map {
                UserAnswerRequest(it.key, it.value)
            }
            val request = SubmitQuizRequest(answerList)

            try {
                val response = repository.submitQuiz(request)
                if (response.isSuccessful && response.body() != null) {

                    resultState = ResultUiState.Success(response.body()!!)
                } else {
                    resultState = ResultUiState.Error("Gagal submit jawaban")
                }
            } catch (e: Exception) {
                resultState = ResultUiState.Error(e.message ?: "Error koneksi")
            }
        }
    }
}