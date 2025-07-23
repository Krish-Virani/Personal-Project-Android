package com.example.personalproject.data.repositoryImpl

import com.example.personalproject.data.remotedata.auth.LoginResponse
import com.example.personalproject.data.remotedata.dealer.AlloyRequest
import com.example.personalproject.data.remotedata.dealer.AlloyResponse
import com.example.personalproject.data.remotedata.dealer.DealerApi
import com.example.personalproject.data.remotedata.dealer.DealerModifyRequest
import com.example.personalproject.domain.model.User
import com.example.personalproject.domain.repository.DealerRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class DealerRepositoryImpl @Inject constructor(
    private val dealerApi : DealerApi
): DealerRepository {

    override suspend fun getAllAlloysOfDealer(token: String): List<AlloyResponse> {
        return dealerApi.getAllAlloysOfDealer(token)
    }

    override suspend fun getAlloyByIdOfDealer(
        token: String,
        id: String
    ): AlloyResponse {
        return dealerApi.getAlloyByIdOfDealer(token, id)
    }

    override suspend fun addAlloy(token: String, alloyRequest: AlloyRequest): String {
        return dealerApi.addAlloy(token, alloyRequest)
    }

    override suspend fun getProfile(token: String, id: String?): User {
        return dealerApi.getProfile(token, id)
    }

    override suspend fun editAlloy(
        token: String,
        id: String?,
        alloyRequest: AlloyRequest
    ): String {
        return dealerApi.editAlloy(token, id, alloyRequest)
    }

    override suspend fun modifyDealer(
        token: String,
        id: String?,
        dealerModifyRequest: DealerModifyRequest
    ): LoginResponse {
        return dealerApi.modifyDealer(token, id, dealerModifyRequest)
    }

    override suspend fun uploadAlloyImage(
        id: String?,
        file: MultipartBody.Part,
        token: String
    ): String {
        return dealerApi.uploadAlloyImage(id,file,token)
    }


}