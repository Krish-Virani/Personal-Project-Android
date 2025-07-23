package com.example.personalproject.domain.repository

import com.example.personalproject.data.remotedata.car.CarBrandResponse
import com.example.personalproject.data.remotedata.car.CarModelResponse

interface CarRepository {

    suspend fun getCarBrands(): List<CarBrandResponse>

    suspend fun getCarModelsByBrand(id: String): List<CarModelResponse>
}