package com.example.alpvp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.alpvp.ui.uistate.HomeUIState
import com.example.alpvp.ui.viewmodel.HomeViewModel

// ... (Import tetap sama)
@Composable
fun HomeView(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val state = viewModel.homeUIState
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                // Tombol Home -> Ke DashboardView
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
                // Tombol Materi (SEDANG AKTIF DI SINI)
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Materi") },
                    label = { Text("Materi") },
                    selected = true, // <-- Nyala karena ini halaman materi
                    onClick = { }
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
        // ... (Isi konten Grid Makanan JANGAN DIUBAH, biarkan kode lamamu di sini)
        // Cukup copas body content dari kode HomeView lamamu
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F6FA))
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // ... Header Materi & Grid Makanan ...
            // (Gunakan kode lama untuk bagian Column ini)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp, top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🥗 ", fontSize = 20.sp)
                    Text(
                        text = "Healthy Quiz",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9C27B0)
                    )
                }
                Text(
                    text = "Makanan Sehat",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Grid Content
            when (state) {
                is HomeUIState.Loading -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                is HomeUIState.Error -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { Text(state.message) }

                is HomeUIState.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(state.foods.distinctBy { it.id }) { food ->
                            FoodCard(
                                name = food.name,
                                category = food.category,
                                imageUrl = food.image_url,
                                onClick = { navController.navigate("detail/${food.id}") }
                            )
                        }
                    }
                }
                else -> {}
            }
        }
    }
}
// Function FoodCard biarkan saja di bawah

@Composable
fun FoodCard(name: String, category: String, imageUrl: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(2.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = name,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = category,
                fontSize = 14.sp,
                color = Color(0xFF4CAF50),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "More Detail",
                color = Color(0xFFAB47BC),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}