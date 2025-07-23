package com.example.personalproject.data.remotedata.dealer

data class AlloyResponse(
    val id: String = "",

    val size: Double = 0.0,

    val name: String = "",

    val width: Double = 0.0,

    val offset: Int = 0,

    val pcd: String = "",

    val price: Double = 0.0,

    val imageUrl: String = "",

    val imageKey: String = "",

    val compatibleModels: MutableList<Pair<String, Pair<String?, String>?>>? = null
)