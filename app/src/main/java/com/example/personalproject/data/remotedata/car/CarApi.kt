package com.example.personalproject.data.remotedata.car

import retrofit2.http.GET
import retrofit2.http.Path

interface CarApi {

    @GET("/public/carbrands")
    suspend fun getCarBrands(): List<CarBrandResponse>

    @GET("/public/carmodels/{id}")
    suspend fun getCarModelsByBrand(
        @Path("id") id: String
    ): List<CarModelResponse>
}