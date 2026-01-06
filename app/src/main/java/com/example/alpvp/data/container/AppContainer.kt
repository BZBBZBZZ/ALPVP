package com.example.alpvp.data.container

import com.example.alpvp.data.repository.UserRepository

interface AppContainer {
    val userRepository: UserRepository
}
