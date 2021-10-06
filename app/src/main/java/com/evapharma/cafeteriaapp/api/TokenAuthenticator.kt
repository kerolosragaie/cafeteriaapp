package com.evapharma.cafeteriaapp.api

import android.content.Context
import com.evapharma.cafeteriaapp.models.UserResponse
import com.evapharma.cafeteriaapp.services.LoginService
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(private val context:Context): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        val userResponse:UserResponse?= SessionManager(context).fetchAccessToken()


        return response.request
            .newBuilder()
            .header(LoginService.TOKEN_ENDPOINT,userResponse!!.token)
            .build()
    }
}