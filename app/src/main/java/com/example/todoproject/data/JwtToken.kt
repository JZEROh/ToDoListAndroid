package com.example.todoproject.data

data class JwtToken(
    val accessToken: String,
    val refreshToken: String? = null
)
