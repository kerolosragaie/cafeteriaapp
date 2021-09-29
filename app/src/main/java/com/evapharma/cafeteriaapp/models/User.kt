package com.evapharma.cafeteriaapp.models

import android.util.Log


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