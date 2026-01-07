package com.example.alpvp.ui.model

data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val score: Int = 0
)