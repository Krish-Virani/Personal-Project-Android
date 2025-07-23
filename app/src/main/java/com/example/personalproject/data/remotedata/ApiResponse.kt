package com.example.personalproject.data.remotedata

data class ApiResponse<T>(
    val message: String,
    val code: String,
    val data: T? = null
)

