package com.example.personalproject.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalproject.data.DataStoreManager
import com.example.personalproject.data.remotedata.car.CarBrandResponse
import com.example.personalproject.data.remotedata.car.CarModelResponse
import com.example.personalproject.data.remotedata.customer.CAlloyResponse
import com.example.personalproject.domain.repository.CarRepository
import com.example.personalproject.domain.repository.CustomerRepository
import com.example.personalproject.domain.repository.DealerRepository
import com.example.personalproject.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val carRepository: CarRepository,
    private val customerRepository: CustomerRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel(){

    private val _carBrandState = MutableStateFlow<UiState<List<CarBrandResponse>>>(UiState.Idle)
    val carBrandState : StateFlow<UiState<List<CarBrandResponse>>> = _carBrandState.asStateFlow()

    private val _carModelState = MutableStateFlow<UiState<List<CarModelResponse>>>(UiState.Idle)
    val carModelState : StateFlow<UiState<List<CarModelResponse>>> = _carModelState.asStateFlow()
    
    private val _alloyResultState = MutableStateFlow<UiState<List<CAlloyResponse>>>(UiState.Idle)
    val alloyResultState: StateFlow<UiState<List<CAlloyResponse>>> = _alloyResultState.asStateFlow()

    suspend fun getToken() : String? = dataStoreManager.tokenFlow.firstOrNull()

    fun searchAlloys(carModelId: String?, size: Double, width: Double, state: String?, city: String?) {
        viewModelScope.launch {
            _alloyResultState.value = UiState.Loading
            try {
                val result = customerRepository.searchAlloys(
                    token = "Bearer "+getToken(),
                    carModelId,
                    size,
                    width,
                    state,
                    city
                )
                _alloyResultState.value = UiState.Success(result)
            } catch (e: Exception) {
                println("Alloy search error: ${e.message}")
                _alloyResultState.value = UiState.Error(e.message ?: "Unexpected error occurred")
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
                println("Fetched Models: ${response.map { it.modelName }}")
                _carModelState.value = UiState.Success(response)
            } catch (e: Exception) {
                println("Error fetching car models: ${e.message}")
                _carModelState.value = UiState.Error(e.message ?: "Unexpected error occurred")
            }
        }
    }

}