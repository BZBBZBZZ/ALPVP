package com.example.alpvp.data.container

import com.example.alpvp.data.repository.MockUserRepository
import com.example.alpvp.data.repository.UserRepository

/**
 * Test AppContainer yang menggunakan MockUserRepository untuk testing tanpa backend.
 * Gunakan ini untuk debugging Leaderboard UI ketika backend belum ready.
 */
class TestAppContainer : AppContainer {
    override val userRepository: UserRepository = MockUserRepository()
}

