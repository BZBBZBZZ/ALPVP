package com.example.alpvp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.alpvp.ui.uistate.ResultUiState
import com.example.alpvp.ui.viewmodel.QuizViewModel

@Composable
fun ResultView(
    viewModel: QuizViewModel,
    navController: NavController
) {
    val state = viewModel.resultState

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6FA))
            .padding(16.dp)
    ) {
        when (state) {
            is ResultUiState.Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }

            is ResultUiState.Error -> {
                Text(
                    text = "Error: ${state.message}",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is ResultUiState.Success -> {
                val result = state.result

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header Judul
                    Text(
                        text = "Hasil Quiz",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Ikon Medali
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color(0xFFE94057), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🎖️", fontSize = 40.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Teks Motivasi
                    Text(
                        text = "Terus Berlatih! 💪",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Kartu Total Skor
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Total Skor", color = Color.Gray)
                            Text(
                                text = "${result.score}",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6200EE)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // List Pembahasan Soal
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        // Tambahkan padding bawah agar item terakhir tidak tertutup tombol
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        // Gunakan itemsIndexed untuk mendapatkan nomor soal
                        itemsIndexed(result.details ?: emptyList()) { index, detail ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    // 1. Nomor dan Teks Soal
                                    // Menggunakan teks placeholder jika data dari backend belum ada
                                    val questionText = detail.question_text ?: "(Soal tidak tersedia)"
                                    Text(
                                        text = "${index + 1}. $questionText",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // 2. Status Benar/Salah
                                    Text(
                                        text = if (detail.is_correct) "Benar" else "Salah",
                                        color = if (detail.is_correct) Color(0xFF4CAF50) else Color.Red,
                                        fontWeight = FontWeight.Bold
                                    )

                                    // 3. Jawaban Benar
                                    val correctAnswer = detail.correct_answer ?: "-"
                                    Text(
                                        text = "jawaban yang benar adalah ($correctAnswer)",
                                        style = MaterialTheme.typography.bodyMedium
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // 4. Penjelasan (Explanation)
                                    val explanation = detail.explanation ?: "Tidak ada pembahasan."
                                    Text(
                                        text = "($explanation)",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tombol Leaderboard (Hiasan)
                    Button(
                        onClick = { /* TODO */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE94057)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Leaderboard")
                    }

                    // Tombol Kembali ke Beranda
                    OutlinedButton(
                        onClick = {
                            viewModel.resetNavigationFlag()
                            navController.navigate("Quiz") {
                                popUpTo("Result") { inclusive = true }
                            }
                            viewModel.loadQuestions()
                        },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Kembali ke Beranda", color = Color.Black)
                    }
                }
            }
        }
    }
}