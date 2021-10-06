package com.evapharma.cafeteriaapp.models

import java.io.Serializable

data class ProductResponse(
    val category: Any,
    val categoryId: Int,
    val id: Int,
    val imageUrl: String,
    val inOffers: Boolean,
    val name: String,
    val price: Double
):Serializable

data class ProductRequest(
    val imageUrl: String,
    val inOffers: Boolean,
    val name: String,
    val price: Int
)