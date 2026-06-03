package com.example.alpvp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.alpvp.ui.viewmodel.AuthViewModel
import com.example.alpvp.ui.viewmodel.LeaderboardUiState
import com.example.alpvp.ui.viewmodel.LeaderboardViewModel
import androidx.compose.material.icons.automirrored.filled.List

@Composable
fun ProfileView(
    navController: NavController,
    authViewModel: AuthViewModel,
    leaderboardViewModel: LeaderboardViewModel
) {
    val user = authViewModel.currentUser
    val leaderboardState by leaderboardViewModel.leaderboardUiState.collectAsState()

    val userRank = when (val state = leaderboardState) {
        is LeaderboardUiState.Success -> {
            val index = state.users.indexOfFirst { it.user_id == user?.user_id }
            if (index != -1) "#${index + 1}" else "-"
        }
        else -> "-"
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { navController.navigate("home") }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Quiz") },
                    label = { Text("Quiz") },
                    selected = false,
                    onClick = { navController.navigate("Quiz") }
                )

                NavigationBarItem(
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.List,
                            contentDescription = "Materi"
                        )
                    },
                    label = { Text("Materi") },
                    selected = false,
                    onClick = { navController.navigate("materi") }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = "Leaderboard") },
                    label = { Text("Top") },
                    selected = false,
                    onClick = { navController.navigate("leaderboard") }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Profil Saya",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = user?.username ?: "User",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9C27B0)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Data akun kamu",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    ProfileRow(label = "User ID", value = user?.user_id?.toString() ?: "-")
                    ProfileRow(label = "Username", value = user?.username ?: "-")
                    ProfileRow(label = "Highest Score", value = user?.high_score?.toString() ?: "0")
                    ProfileRow(label = "Last Played", value = user?.last_played_at ?: "Belum pernah bermain")
                    ProfileRow(label = "Rank", value = userRank)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
            ) {
                Text("Logout", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ProfileRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF333333))
        Spacer(modifier = Modifier.height(12.dp))
    }
}

