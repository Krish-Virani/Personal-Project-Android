package com.example.personalproject.data.repositoryImpl

import com.example.personalproject.data.remotedata.ApiResponse
import com.example.personalproject.data.remotedata.auth.AuthApi
import com.example.personalproject.data.remotedata.auth.LoginRequest
import com.example.personalproject.data.remotedata.auth.LoginResponse
import com.example.personalproject.data.remotedata.auth.SignupRequest
import com.example.personalproject.domain.repository.AuthApiRepository
import javax.inject.Inject

class AuthApiRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
): AuthApiRepository {

    override suspend fun signup(request: SignupRequest): ApiResponse<LoginResponse> {
        return authApi.signup(request)
    }

    override suspend fun login(request: LoginRequest): ApiResponse<LoginResponse> {
        return authApi.login(request)
    }
}