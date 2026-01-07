package com.example.alpvp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Data Dummy untuk Leaderboard
data class Player(val rank: Int, val name: String, val score: Int)

val dummyLeaderboard = listOf(
    Player(1, "Budi Santoso", 1200),
    Player(2, "Siti Aminah", 1150),
    Player(3, "Dave Sachio", 1100), // Nama kamu ada di sini 😉
    Player(4, "Joko Anwar", 980),
    Player(5, "Rina Nose", 950),
    Player(6, "Deddy Corbuzier", 890),
    Player(7, "Raffi Ahmad", 850),
    Player(8, "Nagita Slavina", 800),
    Player(9, "Sule Prikitiw", 760),
    Player(10, "Andre Taulany", 720)
)

@Composable
fun LeaderboardView(navController: NavController) {
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                // Tombol Home -> Ke Dashboard
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { navController.navigate("home") }
                )
                // Tombol Quiz
                NavigationBarItem(
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Quiz") },
                    label = { Text("Quiz") },
                    selected = false,
                    onClick = { navController.navigate("Quiz") }
                )
                // Tombol Materi -> Ke HomeView.kt
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Materi") },
                    label = { Text("Materi") },
                    selected = false,
                    onClick = { navController.navigate("materi") }
                )
                // Tombol Leaderboard (AKTIF)
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = "Leaderboard") },
                    label = { Text("Leaderboard") },
                    selected = true,
                    onClick = { }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F6FA))
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Header Judul
            Text(
                text = "🏆 Leaderboard",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE94057),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Top 3 Cards (Podium)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                // Juara 2
                WinnerPodium(player = dummyLeaderboard[1], height = 120.dp, color = Color(0xFFC0C0C0))
                // Juara 1
                WinnerPodium(player = dummyLeaderboard[0], height = 150.dp, color = Color(0xFFFFD700))
                // Juara 3
                WinnerPodium(player = dummyLeaderboard[2], height = 100.dp, color = Color(0xFFCD7F32))
            }

            // List Sisanya (Peringkat 4-10)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // .drop(3) artinya melewati 3 data pertama (karena sudah di podium)
                itemsIndexed(dummyLeaderboard.drop(3)) { index, player ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "#${player.rank}",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.width(35.dp),
                                    color = Color.Gray
                                )
                                Text(
                                    text = player.name,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                )
                            }
                            Text(
                                text = "${player.score} pts",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE94057)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Komponen Helper untuk membuat Podium
@Composable
fun WinnerPodium(player: Player, height: androidx.compose.ui.unit.Dp, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Nama Depan
        Text(
            text = player.name.split(" ")[0],
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Kotak Tiang Podium
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(height)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${player.rank}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Skor
        Text(
            text = "${player.score}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE94057)
        )
    }
}