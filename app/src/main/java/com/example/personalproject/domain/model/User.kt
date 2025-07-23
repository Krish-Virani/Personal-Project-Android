package com.example.personalproject.domain.model

data class User(
    val name: String,
    val phoneNumber: String,
    val role: String,
    val dealerProfile: DealerProfile,
    val createdAt: String
)
