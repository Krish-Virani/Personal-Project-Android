package com.example.personalproject.domain.repository

import com.example.personalproject.data.remotedata.ApiResponse
import com.example.personalproject.data.remotedata.auth.LoginRequest
import com.example.personalproject.data.remotedata.auth.LoginResponse
import com.example.personalproject.data.remotedata.auth.SignupRequest

interface AuthApiRepository {

    suspend fun signup(request: SignupRequest): ApiResponse<LoginResponse>

    suspend fun login(request: LoginRequest): ApiResponse<LoginResponse>
}