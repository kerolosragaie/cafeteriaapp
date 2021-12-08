package com.evapharma.cafeteriaapp.models

import java.io.Serializable

data class Order(
    val id: Int,
    val isReady: Boolean,
    val orderDate: String,
    val orderItems: List<Any>,
    val userId: String,
):Serializable

data class OrderRequest(
    var isReady: Boolean?=null
)

data class OrderItems(
    val id: Int,
    val order: Any,
    val orderId: Int,
    val product: ProductResponse,
    val productId: Int,
    val quantity: Int
)

