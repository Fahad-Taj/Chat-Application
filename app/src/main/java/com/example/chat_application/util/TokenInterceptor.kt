package com.example.chat_application.util

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor() : Interceptor {
    private var sessionCookie: String? = null


    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()

        sessionCookie?.let {
            requestBuilder.addHeader("Cookie", it)
        }

        val response = chain.proceed(requestBuilder.build())

        response.headers("set-cookie").firstOrNull { it.startsWith("access_token=") }?.let {
            sessionCookie = it.split(";").first()
            access_token= sessionCookie!!.split("=")[1]
            Log.e("accrss", access_token.toString())
        }

        return response
    }
    fun getSessionCookie(): String? = sessionCookie

}