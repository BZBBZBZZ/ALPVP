package com.example.alpvp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    // Ambil state hasil dari ViewModel
    val state = viewModel.resultState

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6FA)) // Background abu-abu muda
            .padding(16.dp)
    ) {
        when (state) {
            // 1. Tampilan Loading
            is ResultUiState.Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }

            // 2. Tampilan Error
            is ResultUiState.Error -> {
                Text(
                    text = "Error: ${state.message}",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // 3. Tampilan Sukses (Hasil Kuis)
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

                    // Ikon Medali (Lingkaran Merah)
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
                                color = Color(0xFF6200EE) // Warna Ungu
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // List Pembahasan Soal (LazyColumn)
                    // Modifier.weight(1f) agar list mengambil sisa ruang yang ada
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(result.details) { detail ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {

                                    // Status Benar/Salah
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (detail.is_correct) "✅ Benar" else "❌ Salah",
                                            fontWeight = FontWeight.Bold,
                                            color = if (detail.is_correct) Color(0xFF4CAF50) else Color.Red
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // Jawaban User (Diberi pengaman ?:)
                                    Text(text = "Jawaban kamu: ${detail.user_answer ?: "-"}")

                                    // Kunci Jawaban (Diberi pengaman ?:)
                                    Text(
                                        text = "Kunci: ${detail.correct_answer ?: "-"}",
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Penjelasan (ANTI CRASH: Menggunakan Elvis Operator)
                                    // Jika explanation null, tampilkan "Tidak ada pembahasan"
                                    Text(
                                        text = detail.explanation ?: "Tidak ada pembahasan.",
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tombol Leaderboard (Hiasan)
                    Button(
                        onClick = { /* TODO: Arahkan ke Leaderboard */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE94057)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Leaderboard")
                    }

                    // Tombol Kembali ke Beranda (Reset Quiz)
                    OutlinedButton(
                        onClick = {
                            viewModel.resetNavigationFlag()
                            // Navigasi kembali ke halaman Quiz (Restart)
                            navController.navigate("Quiz") {
                                // Hapus history Result agar tidak bisa diback balik kesini
                                popUpTo("Result") { inclusive = true }
                            }
                            // Reset state di ViewModel agar mulai dari awal lagi
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