package com.example.personalproject.data.remotedata.auth

import com.example.personalproject.data.remotedata.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/auth/signup")
    suspend fun signup(@Body request: SignupRequest): ApiResponse<LoginResponse>

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResponse>
}