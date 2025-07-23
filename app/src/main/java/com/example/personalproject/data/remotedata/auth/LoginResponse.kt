package com.example.personalproject.data.remotedata.auth

data class LoginResponse(
    val token: String,
    val role: String,
    val userId: String
)