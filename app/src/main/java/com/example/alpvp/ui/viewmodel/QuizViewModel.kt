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
    private val repository = AppContainer().quizRepository

    var quizState: QuizUiState by mutableStateOf(QuizUiState.Loading)
        private set

    var resultState: ResultUiState by mutableStateOf(ResultUiState.Loading)
        private set

    var hasNavigatedToResult = false

    private var timerJob: Job? = null

    init {
        loadQuestions()
    }

    // --- FUNGSI BARU UNTUK MAIN LAGI ---
    fun restartQuiz() {
        // 1. Stop timer lama jika masih jalan (safety measure)
        timerJob?.cancel()

        // 2. Reset navigasi flag
        hasNavigatedToResult = false

        // 3. Reset result state ke Loading (supaya tidak otomatis pindah ke ResultView lagi)
        resultState = ResultUiState.Loading

        // 4. Muat ulang soal (ini akan otomatis mengacak soal karena logika di loadQuestions)
        loadQuestions()
    }

    fun loadQuestions() {
        viewModelScope.launch {
            quizState = QuizUiState.Loading
            try {
                val response = repository.getAllQuestions()
                if (response.isSuccessful && response.body() != null) {
                    // === PERUBAHAN DISINI: Tambahkan .shuffled() ===
                    // Agar setiap kali main, urutan soalnya beda
                    val questions = response.body()!!.data.shuffled()

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
            while (true) {
                delay(1000L)
                val currentState = quizState

                if (currentState is QuizUiState.Success) {
                    if (currentState.timeLeft > 0) {
                        quizState = currentState.copy(timeLeft = currentState.timeLeft - 1)
                    } else {
                        answerQuestion("")
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
            val currentQ = currentState.questions[currentState.currentQuestionIndex]
            currentState.userAnswers[currentQ.id] = answer

            if (currentState.currentQuestionIndex < currentState.questions.size - 1) {
                quizState = currentState.copy(
                    currentQuestionIndex = currentState.currentQuestionIndex + 1,
                    timeLeft = 10
                )
                startTimer()
            } else {
                timerJob?.cancel()
                resultState = ResultUiState.Loading
                submitQuiz(currentState)
            }
        }
    }

    fun resetNavigationFlag() {
        hasNavigatedToResult = false
    }

    private fun submitQuiz(finalState: QuizUiState.Success) {
        viewModelScope.launch {
            val answerList = finalState.userAnswers.map {
                UserAnswerRequest(it.key, it.value)
            }
            val request = SubmitQuizRequest(answerList)

            try {
                val response = repository.submitQuiz(request)
                if (response.isSuccessful && response.body() != null) {
                    val wrapper = response.body()!!
                    val realResult = wrapper.data
                    resultState = ResultUiState.Success(realResult)
                } else {
                    resultState = ResultUiState.Error("Gagal submit: ${response.code()}")
                }
            } catch (e: Exception) {
                resultState = ResultUiState.Error(e.message ?: "Error koneksi")
            }
        }
    }
}