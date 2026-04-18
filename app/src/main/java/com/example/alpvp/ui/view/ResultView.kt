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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding

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
            .windowInsetsPadding(WindowInsets.statusBars)
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
                    // 1. HEADER
                    Text(
                        text = "Hasil Quiz",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. MEDALI
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color(0xFFE94057), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🎖️", fontSize = 40.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Terus Berlatih! 💪",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 3. KARTU SKOR
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4. LIST PEMBAHASAN
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f) // Mengambil sisa ruang yang tersedia
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 10.dp)
                    ) {
                        itemsIndexed(result.details ?: emptyList()) { index, detail ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {

                                    // Nomor dan Teks Soal
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Text(
                                            text = "${index + 1}. ",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = detail.question_text ?: "Memuat soal...",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 15.sp,
                                            color = Color.Black,
                                            lineHeight = 20.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                    HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Status Jawaban
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Jawabanmu: ", fontSize = 13.sp, color = Color.Gray)
                                        Text(
                                            text = detail.user_answer ?: "-",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (detail.is_correct) Color(0xFF4CAF50) else Color.Red
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (detail.is_correct) "✅" else "❌",
                                            fontSize = 12.sp
                                        )
                                    }

                                    // Kunci Jawaban (jika salah)
                                    if (!detail.is_correct) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Kunci: ${detail.correct_answer}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF4CAF50)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Pembahasan
                                    Text(
                                        text = "Pembahasan:",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = detail.explanation ?: "Tidak ada pembahasan.",
                                        fontSize = 13.sp,
                                        color = Color.DarkGray,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 5. TOMBOL NAVIGASI BAWAH

                    // --- [BARU] TOMBOL MAIN LAGI ---
                    Button(
                        onClick = {
                            // Reset state dan timer di ViewModel
                            viewModel.restartQuiz()

                            // Navigasi kembali ke Quiz
                            navController.navigate("Quiz") {
                                // Pop Result agar kalau di-back dari Quiz tidak balik ke sini
                                popUpTo("home") { inclusive = false }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Warna Hijau
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Main Lagi 🔄")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- TOMBOL LEADERBOARD ---
                    Button(
                        onClick = { navController.navigate("leaderboard") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE94057)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Lihat Leaderboard")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- TOMBOL KEMBALI KE BERANDA ---
                    OutlinedButton(
                        onClick = {
                            viewModel.resetNavigationFlag()
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                            // Reset/Load ulang questions jika diperlukan untuk sesi berikutnya
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