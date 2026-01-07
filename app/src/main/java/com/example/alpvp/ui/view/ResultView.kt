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
import androidx.compose.foundation.layout.WindowInsets // Pastikan import ini ada
import androidx.compose.foundation.layout.statusBars // Pastikan import ini ada
import androidx.compose.foundation.layout.windowInsetsPadding // Pastikan import ini ada

@Composable
fun ResultView(
    viewModel: QuizViewModel,
    navController: NavController
) {
    val state = viewModel.resultState

    // --- PERBAIKAN DI SINI (BOX UTAMA) ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6FA))
            // Tambahkan baris ini agar konten turun melewati status bar/kamera
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
                            .weight(1f)
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
                                    val teksSoal = detail.question_text ?: "Soal No. ${index + 1}"
                                    Text(
                                        text = "${index + 1}. $teksSoal",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Status Benar/Salah
                                    Text(
                                        text = if (detail.is_correct) "✅ Benar" else "❌ Salah",
                                        color = if (detail.is_correct) Color(0xFF4CAF50) else Color.Red,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )

                                    // Jika Salah, Tampilkan Jawaban yang Benar
                                    if (!detail.is_correct) {
                                        Text(
                                            text = "Jawaban yang benar adalah (${detail.correct_answer})",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.DarkGray
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // Explanation
                                    Text(
                                        text = "(${detail.explanation ?: "Tidak ada pembahasan"})",
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                        }
                    }

                    // 5. TOMBOL AKSI
                    Button(
                        onClick = { /* TODO */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE94057)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Leaderboard")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

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