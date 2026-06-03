package com.example.alpvp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.alpvp.ui.uistate.QuizUiState
import com.example.alpvp.ui.uistate.ResultUiState
import com.example.alpvp.ui.viewmodel.QuizViewModel
import androidx.compose.foundation.layout.WindowInsets // Import Wajib 1
import androidx.compose.foundation.layout.statusBars // Import Wajib 2
import androidx.compose.foundation.layout.windowInsetsPadding // Import Wajib 3

@Composable
fun QuizView(
    viewModel: QuizViewModel,
    navController: NavController,
    username: String? = null
) {
    val state = viewModel.quizState
    val resultState = viewModel.resultState
    val isPlaying = viewModel.isPlaying
    val hasQuestions = state is QuizUiState.Success && state.questions.isNotEmpty()

    LaunchedEffect(username) {
        viewModel.setCurrentUsername(username)
    }

    // Cek jika result sudah sukses, pindah halaman
    androidx.compose.runtime.LaunchedEffect(resultState) {
        if (resultState is ResultUiState.Success && !viewModel.hasNavigatedToResult) {
            navController.navigate("Result") {
                popUpTo("Quiz") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6FA)) // Background abu muda
            // --- PERBAIKAN DI SINI (Supaya Timer tidak ketutup kamera) ---
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(16.dp)
    ) {
        when {
            state is QuizUiState.Error -> Text("Error: ${state.message}", Modifier.align(Alignment.Center))

            !isPlaying -> {
                Card(
                    modifier = Modifier.align(Alignment.Center),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .widthIn(max = 320.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Siap Mulai Quiz?",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tekan tombol di bawah untuk mulai bermain. Kamu akan diberi waktu 10 detik tiap soal.",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            lineHeight = 20.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { if (hasQuestions) viewModel.startPlaying() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                            enabled = hasQuestions
                        ) {
                            Text("Start Playing", fontWeight = FontWeight.Bold)
                        }

                        if (state is QuizUiState.Success && state.questions.isEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Soal belum tersedia. Coba isi data quiz di backend dulu.",
                                color = Color.Red,
                                fontSize = 12.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }

                        if (state is QuizUiState.Loading) {
                            Spacer(modifier = Modifier.height(12.dp))
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            state is QuizUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

            state is QuizUiState.Success -> {
                if (state.questions.isEmpty()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Soal kosong",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Data quiz belum ada di backend.",
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                    return
                }

                val currentQ = state.questions[state.currentQuestionIndex]

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header
                    Text("Sayuran", color = Color.Gray, fontSize = 14.sp)
                    Text(
                        "Pertanyaan ${state.currentQuestionIndex + 1} dari ${state.questions.size}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    // Progress Bar Hijau
                    LinearProgressIndicator(
                        progress = (state.currentQuestionIndex + 1) / state.questions.size.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .height(8.dp),
                        color = Color(0xFF4CAF50),
                        trackColor = Color.LightGray
                    )

                    // Timer & Score Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Timer Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Simbol Jam (Text aja biar simple)
                                Text("🕒", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Waktu", fontSize = 10.sp, color = Color.Gray)
                                    Text("${state.timeLeft} S", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Score Card (Dummy 0)
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🏆", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Skor", fontSize = 10.sp, color = Color.Gray)
                                    // Skor real-time belum ada di state, jadi sementara 0
                                    Text("0", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Question Card (Putih Besar)
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Box(
                            modifier = Modifier.padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentQ.question_text,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Options Buttons
                    val options = listOf(
                        "a" to currentQ.option_a,
                        "b" to currentQ.option_b,
                        "c" to currentQ.option_c,
                        "d" to currentQ.option_d
                    )
                    options.forEach { (key, text) ->
                        Button(
                            onClick = { viewModel.answerQuestion(key) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Text(
                                text = text,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Start
                            )
                        }
                    }
                }
            }
        }
    }
}