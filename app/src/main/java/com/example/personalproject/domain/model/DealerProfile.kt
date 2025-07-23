package com.example.personalproject.domain.model

data class DealerProfile(
    val shopName: String,
    val shopCity: String,
    val shopState: String,
    val shopImage: String? = null,
    val isVerified: Boolean = false,
    val likes: Long =0
)
