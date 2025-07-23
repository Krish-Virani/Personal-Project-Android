package com.example.personalproject.data.repositoryImpl

import com.example.personalproject.data.remotedata.car.CarApi
import com.example.personalproject.data.remotedata.car.CarBrandResponse
import com.example.personalproject.data.remotedata.car.CarModelResponse
import com.example.personalproject.domain.repository.CarRepository
import javax.inject.Inject

class CarRepositoryImpl @Inject constructor(
    private val carApi: CarApi
): CarRepository {
    override suspend fun getCarBrands(): List<CarBrandResponse> {
        return carApi.getCarBrands()
    }

    override suspend fun getCarModelsByBrand(id: String): List<CarModelResponse> {
        return carApi.getCarModelsByBrand(id)
    }

}