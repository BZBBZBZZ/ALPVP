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
        viewModelScope.launch { //buat manggil API di background thread
            quizState = QuizUiState.Loading
            try {
                val response = repository.getAllQuestions() //buat get /api/questions
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
            // nyimpen jawaban
            val currentQ = currentState.questions[currentState.currentQuestionIndex]
            currentState.userAnswers[currentQ.id] = answer

            // cek ada soal lain nya g
            if (currentState.currentQuestionIndex < currentState.questions.size - 1) {
                // lanjut next soal, reset timer
                quizState = currentState.copy(
                    currentQuestionIndex = currentState.currentQuestionIndex + 1,
                    timeLeft = 10 // reset timer ke 10 detik
                )
                startTimer()
                // timer akan otomatis lanjut karena masih dalam state Success
            } else {
                // soal abis, submit keserver
                timerJob?.cancel()
                // nge set state ke loading buat timer stop
                resultState = ResultUiState.Loading
                submitQuiz(currentState)
            }
        }
    }

    private fun submitQuiz(finalState: QuizUiState.Success) {
        viewModelScope.launch {
            // convert jawaban map ke list request
            val answerList = finalState.userAnswers.map {
                UserAnswerRequest(it.key, it.value)
            }
            val request = SubmitQuizRequest(answerList)

            try {
                // Panggil Repository
                val response = repository.submitQuiz(request)

                // Cek apakah sukses dan body tidak null
                if (response.isSuccessful && response.body() != null) {

                    // === PERBAIKAN DISINI ===
                    // Kita BUKA WRAPPER-nya dulu untuk ambil isinya (.data)
                    val wrapper = response.body()!!
                    val realResult = wrapper.data

                    // Masukkan data asli (realResult) ke state
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