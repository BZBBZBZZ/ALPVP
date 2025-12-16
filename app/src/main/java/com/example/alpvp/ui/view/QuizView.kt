package com.example.alpvp.ui.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

@Composable
fun QuizView(
    viewModel: QuizViewModel,
    navController: NavController
) {
    val state = viewModel.quizState
    val resultState = viewModel.resultState

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
            .padding(16.dp)
    ) {
        when (state) {
            is QuizUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            is QuizUiState.Error -> Text("Error: ${state.message}", Modifier.align(Alignment.Center))
            is QuizUiState.Success -> {
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