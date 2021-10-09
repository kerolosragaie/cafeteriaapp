package com.evapharma.cafeteriaapp.services

import com.evapharma.cafeteriaapp.models.UserRequest
import com.evapharma.cafeteriaapp.models.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("auth/login")
    fun userLogin(@Body userRequest: UserRequest) : Call<UserResponse>
}