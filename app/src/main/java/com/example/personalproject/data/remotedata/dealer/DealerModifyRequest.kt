package com.example.personalproject.data.remotedata.dealer

import com.example.personalproject.domain.model.DealerProfile

data class DealerModifyRequest(
    val name: String,
    val phoneNumber: String,
    val dealerProfile: DealerProfile
)
