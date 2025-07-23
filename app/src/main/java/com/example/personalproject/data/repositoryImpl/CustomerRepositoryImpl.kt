package com.example.personalproject.data.repositoryImpl

import com.example.personalproject.data.remotedata.customer.CAlloyResponse
import com.example.personalproject.data.remotedata.customer.CustomerApi
import com.example.personalproject.domain.repository.CustomerRepository

class CustomerRepositoryImpl(
    private val customerApi: CustomerApi
): CustomerRepository {
    override suspend fun searchAlloys(
        token: String,
        carModelId: String?,
        size: Double,
        width: Double, state: String?, city: String?
    ): List<CAlloyResponse> {
        return customerApi.searchAlloys(token,carModelId,size,width,state,city)
    }
}