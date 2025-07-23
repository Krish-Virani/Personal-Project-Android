package com.example.personalproject.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalproject.data.DataStoreManager
import com.example.personalproject.data.remotedata.auth.LoginRequest
import com.example.personalproject.data.remotedata.auth.LoginResponse
import com.example.personalproject.data.remotedata.auth.SignupRequest
import com.example.personalproject.domain.repository.AuthApiRepository
import com.example.personalproject.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authApiRepository: AuthApiRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel(){

    private val _authState = MutableStateFlow<UiState<LoginResponse>>(UiState.Idle)
    val authState : StateFlow<UiState<LoginResponse>> = _authState.asStateFlow()

    fun signup(request: SignupRequest){
        viewModelScope.launch {
            _authState.value = UiState.Loading
            try {

                val response = authApiRepository.signup(request)
                if(response.data!=null){

                    val loginResponse = response.data
                    dataStoreManager.saveAuthData(loginResponse.token, loginResponse.userId, loginResponse.role)
                    _authState.value = UiState.Success(loginResponse)
                } else{
                    _authState.value = UiState.Error( response.message)
                }

            } catch (e: Exception){
                _authState.value = UiState.Error(e.message)
            }
        }
    }

    fun login(request: LoginRequest){
        viewModelScope.launch {
            _authState.value = UiState.Loading

            try {

                val response = authApiRepository.login(request)
                if(response.data!=null){

                    val loginResponse = response.data
                    dataStoreManager.saveAuthData(loginResponse.token, loginResponse.userId, loginResponse.role)
                    _authState.value = UiState.Success(loginResponse)
                } else{
                    _authState.value = UiState.Error( response.message)
                }

            } catch (e: Exception){
                _authState.value = UiState.Error(e.message)
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            dataStoreManager.clear()
        }
    }
}