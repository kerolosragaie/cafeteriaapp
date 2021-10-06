package com.evapharma.cafeteriaapp.models

import java.io.Serializable


data class CategoryResponse(
    val id: Int,
    val imageUrl: String,
    val name: String,
    val products: List<Any>
):Serializable


data class CategoryRequest(
    val imageUrl: String,
    val name: String
)
