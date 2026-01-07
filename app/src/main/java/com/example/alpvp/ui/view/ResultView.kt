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
// Import penting untuk status bar
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding

@Composable
fun ResultView(
    viewModel: QuizViewModel,
    navController: NavController
) {
    val state = viewModel.resultState

    // Box Utama dengan padding status bar
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6FA))
            .windowInsetsPadding(WindowInsets.statusBars) // <-- PENTING: Biar gak ketabrak status bar
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

                    // 2. MEDALI / IKON SKOR
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

                    // 3. KARTU TOTAL SKOR
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

                    // 4. LIST PEMBAHASAN SOAL
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        itemsIndexed(result.details ?: emptyList()) { index, detail ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {

                                    // --- NOMOR & TEKS SOAL ---
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        // Nomor (Index + 1)
                                        Text(
                                            text = "${index + 1}. ",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = Color.Black
                                        )

                                        // Teks Soal
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

                                    // --- STATUS JAWABAN ---
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

                                    // Jika Salah, Tampilkan Kunci
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

                                    // --- PEMBAHASAN ---
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

                    // 5. TOMBOL NAVIGASI BAWAH
                    Button(
                        onClick = { /* TODO: Arahkan ke Leaderboard nanti */ },
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
                            // NAVIGASI KE HOME, BUKAN KE QUIZ
                            navController.navigate("home") {
                                // Hapus semua tumpukan history sampai halaman home
                                popUpTo("home") { inclusive = true }
                            }
                            // Opsional: Jika ingin refresh soal saat nanti main lagi
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