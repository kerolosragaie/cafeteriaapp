package com.evapharma.cafeteriaapp.models

import java.io.Serializable

data class UserRequest(
    val email: String,
    val password: String
)

data class UserResponse(
    val email: String,
    val expiresOn: String,
    val isAuthenticated: Boolean,
    val message: Any,
    val roles: List<String>,
    val token: String,
    val username: String
):Serializable


