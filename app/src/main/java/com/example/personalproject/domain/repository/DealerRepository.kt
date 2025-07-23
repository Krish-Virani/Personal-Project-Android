package com.example.personalproject.domain.repository

import com.example.personalproject.data.remotedata.auth.LoginResponse
import com.example.personalproject.data.remotedata.dealer.AlloyRequest
import com.example.personalproject.data.remotedata.dealer.AlloyResponse
import com.example.personalproject.data.remotedata.dealer.DealerModifyRequest
import com.example.personalproject.domain.model.User
import okhttp3.MultipartBody

interface DealerRepository {

    suspend fun getAllAlloysOfDealer(token: String): List<AlloyResponse>

    suspend fun getAlloyByIdOfDealer(token: String, id: String): AlloyResponse

    suspend fun addAlloy(token: String, alloyRequest: AlloyRequest): String

    suspend fun getProfile(token: String, id: String?): User

    suspend fun editAlloy(token: String, id: String?, alloyRequest: AlloyRequest): String

    suspend fun modifyDealer(token: String, id: String?, dealerModifyRequest: DealerModifyRequest): LoginResponse

    suspend fun uploadAlloyImage(id: String?, file: MultipartBody.Part, token: String): String
}