package com.evapharma.cafeteriaapp.services

import com.evapharma.cafeteriaapp.models.Order
import com.evapharma.cafeteriaapp.models.OrderItems
import com.evapharma.cafeteriaapp.models.OrderRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface OrderService {

    @GET("Order/getAllOrders")
    fun getAllOrders(): Call<List<Order>>

    @PUT("Order/adminUpdateOrder")
    fun orderIsReady(@Query("id") id:Int,@Body orderRequest: OrderRequest):Call<Unit>

    @GET("OrderItem/getOrderItems")
    fun getOrderItems(@Query("orderId") orderId:Int):Call<List<OrderItems>>

}