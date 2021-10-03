package com.evapharma.cafeteriaapp.services

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.models.UserResponse
import com.google.gson.Gson

/**
 * Session manager for storing and retrieving sessions from SharedPreferences
 */
class SessionManager(context: Context) {

    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val ACCESS_TOKEN = "access_token"
    }

    /**
     * save access_token
     */
    fun saveAccessToken(userResponse: UserResponse) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(userResponse)
        editor.putString(ACCESS_TOKEN,json).apply()
        Log.d(ACCESS_TOKEN,"Access token saved successfully")
    }

    /**
     * get access_token
     */
    fun fetchAccessToken(): UserResponse? {
        //prefs.getString(ACCESS_TOKEN, null)
        val gson = Gson()
        val json = prefs.getString(ACCESS_TOKEN, null)
        return gson.fromJson(json, UserResponse::class.java)
    }

    /**
     * delete access_token / clear shared preferences
     */
    fun deleteAccessToken() {
        val editor = prefs.edit()
        editor.clear()
            .apply()
        Log.d(ACCESS_TOKEN,"Access token deleted successfully")
    }

}