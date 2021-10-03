package com.evapharma.cafeteriaapp.models

import android.util.Log
import java.io.Serializable


/**
 * A singleton class for user
 * email for user email address
 * password for user password
 * */
object User {
    var name:String = "Kerollos Ragaie"
    var email:String?="kero@gmail.com"
    var password:String?="123456789"

    fun showDataOnLog(logTag:String="UserData"){
        Log.d(logTag,"Email: $email, Password: $password")
    }
}

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


