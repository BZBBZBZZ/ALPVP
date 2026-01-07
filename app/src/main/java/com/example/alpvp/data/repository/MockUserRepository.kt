package com.example.alpvp.data.repository

import com.example.alpvp.ui.model.User

/**
 * Mock repository untuk testing Leaderboard UI tanpa memerlukan server backend.
 * Gunakan ini untuk testing jika backend belum siap.
 */
class MockUserRepository : UserRepository {
    override suspend fun getUsers(): List<User> {
        return listOf(
            User(id = 1, name = "Alice Johnson", username = "alice_j", email = "alice@example.com", score = 950),
            User(id = 2, name = "Bob Smith", username = "bob_smith", email = "bob@example.com", score = 920),
            User(id = 3, name = "Charlie Brown", username = "charlie_b", email = "charlie@example.com", score = 890),
            User(id = 4, name = "Diana Prince", username = "diana_p", email = "diana@example.com", score = 850),
            User(id = 5, name = "Eve Wilson", username = "eve_w", email = "eve@example.com", score = 820),
            User(id = 6, name = "Frank Castle", username = "frank_c", email = "frank@example.com", score = 780),
            User(id = 7, name = "Grace Hopper", username = "grace_h", email = "grace@example.com", score = 750),
            User(id = 8, name = "Henry Ford", username = "henry_f", email = "henry@example.com", score = 720),
        )
    }
}

