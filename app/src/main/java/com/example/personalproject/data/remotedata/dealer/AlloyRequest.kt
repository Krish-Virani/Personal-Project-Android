package com.example.personalproject.data.remotedata.dealer

data class AlloyRequest(
    val name: String,
    val size: Double,
    val width: Double,
    val offset: Int,
    val pcd: String,
    val price: Double,
    val compatibleModels: MutableList<String>?
)
