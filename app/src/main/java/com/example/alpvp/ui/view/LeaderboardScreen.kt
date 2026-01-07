package com.example.alpvp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alpvp.data.container.DefaultAppContainer
import com.example.alpvp.ui.model.User
import com.example.alpvp.ui.viewmodel.LeaderboardUiState
import com.example.alpvp.ui.viewmodel.LeaderboardViewModel
import com.example.alpvp.ui.viewmodel.ViewModelFactory

@Composable
fun LeaderboardScreen(
    // In a real app, you would get the container from the Application class
    // For now, we create it here for simplicity.
    leaderboardViewModel: LeaderboardViewModel = viewModel(factory = ViewModelFactory(DefaultAppContainer()))
) {
    val uiState by leaderboardViewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
    ) {
        // ... (Header Row remains the same)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = "Healthy Quiz", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "Makanan Sehat", fontSize = 14.sp, color = Color.Gray)
            }
        }


        when (val state = uiState) {
            is LeaderboardUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is LeaderboardUiState.Success -> {
                LeaderboardContent(users = state.users)
            }
            is LeaderboardUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Failed to load leaderboard.", color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun LeaderboardContent(users: List<User>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Leaderboard", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(users) { index, user ->
                    LeaderboardCard(rank = index + 1, user = user)
                }
            }
        }
    }
}


@Composable
fun LeaderboardCard(rank: Int, user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F8))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "#$rank",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.width(50.dp) // Adjusted width for rank
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = user.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    // Using username as a substitute for score as it's not in the User DTO
                    Text(text = "@${user.username}", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 800, widthDp = 400)
@Composable
fun LeaderboardScreenPreview() {
    LeaderboardScreen()
}
