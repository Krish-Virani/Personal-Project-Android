package com.example.personalproject.data.remotedata.auth

import com.example.personalproject.domain.model.DealerProfile

data class SignupRequest(
    val name: String,
    val phoneNumber: String,
    val role: String?, // "CUSTOMER" or "DEALER"
    val dealerProfile: DealerProfile? = null
)