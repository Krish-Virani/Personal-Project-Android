package com.example.personalproject.presentation.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalproject.data.DataStoreManager
import com.example.personalproject.data.remotedata.auth.LoginResponse
import com.example.personalproject.data.remotedata.car.CarBrandResponse
import com.example.personalproject.data.remotedata.car.CarModelResponse
import com.example.personalproject.data.remotedata.dealer.AlloyRequest
import com.example.personalproject.data.remotedata.dealer.AlloyResponse
import com.example.personalproject.data.remotedata.dealer.DealerModifyRequest
import com.example.personalproject.domain.model.User
import com.example.personalproject.domain.repository.CarRepository
import com.example.personalproject.domain.repository.DealerRepository
import com.example.personalproject.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DealerViewModel @Inject constructor(
    private val dealerRepository: DealerRepository,
    private val carRepository: CarRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel(){

    private val _hasFetchedAlloys = MutableStateFlow(false)
    val hasFetchedAlloys: StateFlow<Boolean> = _hasFetchedAlloys

    private val _dealerHomeState = MutableStateFlow<UiState<List<AlloyResponse>>>(UiState.Idle)
    val dealerHomeState : StateFlow<UiState<List<AlloyResponse>>> = _dealerHomeState.asStateFlow()

    private val _carBrandState = MutableStateFlow<UiState<List<CarBrandResponse>>>(UiState.Idle)
    val carBrandState : StateFlow<UiState<List<CarBrandResponse>>> = _carBrandState.asStateFlow()

    private val _carModelState = MutableStateFlow<UiState<List<CarModelResponse>>>(UiState.Idle)
    val carModelState : StateFlow<UiState<List<CarModelResponse>>> = _carModelState.asStateFlow()

    private val _addAlloyState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val addAlloyState : StateFlow<UiState<String>> = _addAlloyState.asStateFlow()

    private val _dealerDetailsState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val dealerDetailsState: StateFlow<UiState<User>> = _dealerDetailsState.asStateFlow()

    private val _editAlloyState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val editAlloyState : StateFlow<UiState<String>> = _editAlloyState.asStateFlow()

    private val _alloyDetailsState = MutableStateFlow<UiState<AlloyResponse>>(UiState.Idle)
    val alloyDetailsState : StateFlow<UiState<AlloyResponse>> = _alloyDetailsState.asStateFlow()

    private val _modifyDealerState = MutableStateFlow<UiState<LoginResponse>>(UiState.Idle)
    val modifyDealerState : StateFlow<UiState<LoginResponse>> = _modifyDealerState.asStateFlow()

    private val _alloyImageState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val alloyImageState : StateFlow<UiState<String>> = _alloyImageState.asStateFlow()

    var selectedAlloy : AlloyResponse? = null

    suspend fun getToken() : String? = dataStoreManager.tokenFlow.firstOrNull()

    fun getAlloyByIdOfDealer(id: String){
        viewModelScope.launch {
            _alloyDetailsState.value = UiState.Loading
            try {
                val token = "Bearer "+getToken()
                val response = dealerRepository.getAlloyByIdOfDealer(token, id)
                _alloyDetailsState.value = UiState.Success(response)
            } catch (e: Exception) {
                _alloyDetailsState.value = UiState.Error(e.message ?: "Unexpected error occurred")
            }
        }
    }

    fun getAllAlloysOfDealer(){
        viewModelScope.launch {
            _dealerHomeState.value = UiState.Loading
            try {
                val token = "Bearer "+getToken()
                println(token)
                val response = dealerRepository.getAllAlloysOfDealer(token)
                _dealerHomeState.value = UiState.Success(response)
                _hasFetchedAlloys.value = true
            } catch (e: Exception) {
                _dealerHomeState.value = UiState.Error(e.message ?: "Unexpected error occurred")
            }
        }

    }

    fun getCarBrands(){
        viewModelScope.launch {
            _carBrandState.value = UiState.Loading
            try {
                val response = carRepository.getCarBrands()
                println("Fetched Brands: ${response.map { it.brandName }}")
                _carBrandState.value = UiState.Success(response)
            } catch (e: Exception) {
                println("Error fetching car brands: ${e.message}")
                _carBrandState.value = UiState.Error(e.message ?: "Unexpected error occurred")
            }
        }
    }

    fun getCarModelsByBrand(id: String){
        viewModelScope.launch {
            _carModelState.value = UiState.Loading
            try {
                val response = carRepository.getCarModelsByBrand(id)
                _carModelState.value = UiState.Success(response)
            } catch (e: Exception) {
                _carModelState.value = UiState.Error(e.message ?: "Unexpected error occurred")
            }
        }
    }

    fun addAlloy(alloyRequest: AlloyRequest){
        viewModelScope.launch {
            _addAlloyState.value = UiState.Loading
            try {
                val token = "Bearer "+getToken()
                println(token)
                val response = dealerRepository.addAlloy(token, alloyRequest)
                _addAlloyState.value = UiState.Success(response)
            } catch (e: Exception) {
                _addAlloyState.value = UiState.Error(e.message ?: "Unexpected error occurred")
            }
        }
    }
    fun editAlloy(id: String, alloyRequest: AlloyRequest){
        viewModelScope.launch {
            _editAlloyState.value = UiState.Loading
            try {
                val token = "Bearer "+getToken()
                println(token)
                val response = dealerRepository.editAlloy(token, id, alloyRequest)
                Log.d("viewmodel response", response)
                _editAlloyState.value = UiState.Success(response)
            } catch (e: Exception) {
                _editAlloyState.value = UiState.Error(e.message ?: "Unexpected error occurred")
            }
        }
    }

    fun getUser(){
        viewModelScope.launch {
            _dealerDetailsState.value = UiState.Loading
            try {
                val token = "Bearer "+getToken()
                val id = dataStoreManager.userIdFlow.firstOrNull()
                println(id)
                val user = dealerRepository.getProfile(token, id)
                _dealerDetailsState.value = UiState.Success(user)
            } catch (e: Exception) {
                _dealerDetailsState.value = UiState.Error(e.message ?: "Unexpected error occurred")
            }

        }
    }

    fun modifyDealer(dealerModifyRequest: DealerModifyRequest){
        viewModelScope.launch {
            _modifyDealerState.value = UiState.Loading
            try {
                val token = "Bearer "+getToken()
                val id = dataStoreManager.userIdFlow.firstOrNull()
                val loginResponse = dealerRepository.modifyDealer(token,id, dealerModifyRequest)
                _modifyDealerState.value = UiState.Success(loginResponse)
                dataStoreManager.saveAuthData(loginResponse.token, loginResponse.userId, loginResponse.role)
            } catch (e: Exception) {
                _modifyDealerState.value = UiState.Error(e.message ?: "Unexpected error occurred")
            }

        }
    }

    fun resetModifyDealerState(){
        viewModelScope.launch {
            _modifyDealerState.value = UiState.Idle
        }
    }


    fun resetAddAlloyState() {
        viewModelScope.launch {
            _addAlloyState.value = UiState.Idle
        }

    }

    fun resetEditAlloyState() {
        viewModelScope.launch {
            _editAlloyState.value = UiState.Idle
        }

    }

    fun resetAlloyImageState() {
        viewModelScope.launch {
            _alloyImageState.value = UiState.Idle
        }

    }


    fun uploadCapturedImage(
        context: Context,
        imageUri: Uri,
        alloyId: String,
    ) {
        viewModelScope.launch {
            _alloyImageState.value = UiState.Loading
            try {
                val token = "Bearer "+getToken()
                val file = uriToFile(imageUri, context)
                val mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"
                val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())

                //val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                val multipart = MultipartBody.Part.createFormData("file", file.name, requestBody)
                val response = dealerRepository.uploadAlloyImage(
                    id = alloyId,
                    file = multipart,
                    token = token
                )
                _alloyImageState.value = UiState.Success(response)
            } catch (e: Exception) {
                _alloyImageState.value = UiState.Error(e.message ?: "Unexpected error occurred")
            }
        }
    }

    private fun uriToFile(uri: Uri, context: Context): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File.createTempFile("upload_", ".jpg", context.cacheDir)
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }

    fun resetHasFetchedAlloys() {
        _hasFetchedAlloys.value = false
    }



}