package com.example.personalproject.domain.repository

import com.example.personalproject.data.remotedata.customer.CAlloyResponse

interface CustomerRepository {
    suspend fun searchAlloys(token: String, carModelId: String?, size: Double, width: Double, state: String?, city: String?): List<CAlloyResponse>
}