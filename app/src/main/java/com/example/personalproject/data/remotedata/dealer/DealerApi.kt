package com.example.personalproject.data.remotedata.dealer

import com.example.personalproject.data.remotedata.auth.LoginResponse
import com.example.personalproject.domain.model.User
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface DealerApi {

    @GET("/dealer/get-all-alloys-of-dealer")
    suspend fun getAllAlloysOfDealer(
        @Header("Authorization") token: String
    ): List<AlloyResponse>

    @GET("/dealer/get-alloy-by-id/{id}")
    suspend fun getAlloyByIdOfDealer(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): AlloyResponse

    @POST("/dealer/add-alloy")
    suspend fun addAlloy(
        @Header("Authorization") token: String,
        @Body alloyRequest: AlloyRequest
    ): String

    @GET("dealer/getUser/{id}")
    suspend fun getProfile(
        @Header("Authorization") token: String,
        @Path("id") id: String?
    ): User

    @PUT("/dealer/modify-alloy/{id}")
    suspend fun editAlloy(
        @Header("Authorization") token: String,
        @Path("id") id: String?,
        @Body alloyRequest: AlloyRequest
    ): String

    @PUT("/dealer/modify-dealer/{id}")
    suspend fun modifyDealer(
        @Header("Authorization") token: String,
        @Path("id") id: String?,
        @Body dealerModifyRequest: DealerModifyRequest
    ): LoginResponse

    @Multipart
    @POST("dealer/alloy-image-upload/{alloyId}")
    suspend fun uploadAlloyImage(
        @Path("alloyId") alloyId: String?,
        @Part file: MultipartBody.Part,
        @Header("Authorization") token: String
    ): String
}