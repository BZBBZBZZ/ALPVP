package com.example.alpvp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alpvp.ui.model.User
import com.example.alpvp.ui.viewmodel.LeaderboardUiState
import com.example.alpvp.ui.viewmodel.LeaderboardViewModel
import androidx.navigation.NavController
import com.example.alpvp.data.container.AppContainer
import com.example.alpvp.data.factory.ViewModelFactory

@Composable
fun LeaderboardView(
    navController: NavController,
    viewModel: LeaderboardViewModel = viewModel(factory = ViewModelFactory(AppContainer()))
) {
    val state by viewModel.leaderboardUiState.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                // Tombol Home (Sedang Aktif)
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick =  { navController.navigate("home") }
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
            Text(
                text = "🏆 Leaderboard",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE94057),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when (val currentState = state) {
                is LeaderboardUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is LeaderboardUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = currentState.message, color = Color.Red)
                    }
                }
                is LeaderboardUiState.Success -> {
                    val users = currentState.users
                    // Podium UI
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        if (users.size >= 2) {
                            WinnerPodium(user = users[1], rank = 2, height = 120.dp, color = Color(0xFFC0C0C0))
                        }
                        if (users.isNotEmpty()) {
                            WinnerPodium(user = users[0], rank = 1, height = 150.dp, color = Color(0xFFFFD700))
                        }
                        if (users.size >= 3) {
                            WinnerPodium(user = users[2], rank = 3, height = 100.dp, color = Color(0xFFCD7F32))
                        }
                    }

                    // Rest of the list
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        itemsIndexed(users.drop(3)) { index, user ->
                            LeaderboardItem(user = user, rank = index + 4)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WinnerPodium(user: User, rank: Int, height: androidx.compose.ui.unit.Dp, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = user.username.split(" ").firstOrNull() ?: "",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(height)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#$rank",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${user.high_score ?: 0} pts",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE94057)
        )
    }
}

@Composable
fun LeaderboardItem(user: User, rank: Int) {
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
                    text = "#$rank",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(35.dp),
                    color = Color.Gray
                )
                Text(
                    text = user.username,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
            Text(
                text = "${user.high_score ?: 0} pts",
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE94057)
            )
        }
    }
}