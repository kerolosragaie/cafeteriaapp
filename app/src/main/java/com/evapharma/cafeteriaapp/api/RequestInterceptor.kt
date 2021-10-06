package com.evapharma.cafeteriaapp.api

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor(context: Context) : Interceptor {

    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response{
        val requestBuilder = chain.request().newBuilder()
        // If the token is in the session manager, insert the token in the request header
        sessionManager.fetchAccessToken()?.let {
            requestBuilder.addHeader("Authorization", "$it")
        }

        return chain.proceed(requestBuilder.build())
    }
}