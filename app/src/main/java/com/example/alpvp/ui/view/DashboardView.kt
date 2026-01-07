package com.example.alpvp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.alpvp.ui.viewmodel.AuthUiState
import com.example.alpvp.ui.viewmodel.AuthViewModel
import com.example.alpvp.ui.viewmodel.LeaderboardUiState
import com.example.alpvp.ui.viewmodel.LeaderboardViewModel

@Composable
fun DashboardView(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    leaderboardViewModel: LeaderboardViewModel = viewModel()
) {
    val authState by authViewModel.authUiState.collectAsState()
    val leaderboardState by leaderboardViewModel.leaderboardUiState.collectAsState()

    val user = (authState as? AuthUiState.Success)?.user
    val username = user?.username ?: "User"

    val userRank = when (val lState = leaderboardState) {
        is LeaderboardUiState.Success -> {
            val rank = lState.users.indexOfFirst { it.user_id == user?.user_id }
            if (rank != -1) (rank + 1).toString() else "-"
        }
        else -> "-"
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                // Tombol Home (Sedang Aktif)
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = true,
                    onClick = { }
                )
                // Tombol Quiz
                NavigationBarItem(
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Quiz") },
                    label = { Text("Quiz") },
                    selected = false,
                    onClick = { navController.navigate("Quiz") }
                )
                // Tombol Materi (Arahkan ke file HomeView kamu yang lama)
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Materi") },
                    label = { Text("Materi") },
                    selected = false,
                    onClick = { navController.navigate("materi") }
                )
                // Tombol Leaderboard
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = "Leaderboard") },
                    label = { Text("Leaderboard") },
                    selected = false,
                    onClick = { navController.navigate("leaderboard") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F6FA))
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.5f))
            Text(
                text = "👋 Hai, $username!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Selamat datang di Healthy Quiz. Siap belajar hidup sehat hari ini?",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Card Menu Cepat
            Button(
                onClick = { navController.navigate("Quiz") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Mulai Quiz Sekarang", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { navController.navigate("materi") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pelajari Materi Makanan", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // New Info Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoCard(title = "Best Score", value = user?.high_score?.toString() ?: "-", icon = Icons.Default.EmojiEvents, modifier = Modifier.weight(1f))
                InfoCard(title = "Current Leaderboard", value = userRank, icon = Icons.Default.Leaderboard, modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.weight(1f))

        }
    }
}

@Composable
fun InfoCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = Color(0xFFB14AFF), modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color(0xFF333333))
        }
    }
}