package com.example.personalproject.data.remotedata.customer

import com.example.personalproject.data.remotedata.car.CarBrandResponse
import com.example.personalproject.data.remotedata.car.CarModelResponse
import com.example.personalproject.data.remotedata.dealer.AlloyResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface CustomerApi {

    @GET("/customer/search-alloy")
    suspend fun searchAlloys(
        @Header("Authorization") token: String,
        @Query("carModelId") carModelId: String?,
        @Query("size") size: Double,
        @Query("width") width: Double,
        @Query("state") state: String?,
        @Query("city") city: String?
    ): List<CAlloyResponse>

}