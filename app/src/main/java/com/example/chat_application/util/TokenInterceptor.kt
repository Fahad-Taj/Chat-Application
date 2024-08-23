package com.example.chat_application.util

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    var accessToken: String? = null
        private set

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Add the access token to outgoing requests if available
        val newRequest = if (accessToken != null) {
            originalRequest.newBuilder()
                .addHeader("Cookie", "access_token=$accessToken")
                .addHeader("accept", "application/json")
                .build()
        } else {
            originalRequest
        }

        Log.e("New request", newRequest.toString())
        val response = chain.proceed(newRequest)

        // Extract the token from incoming responses
        val cookies = response.headers("set-cookie")
        for (cookie in cookies) {
            if (cookie.startsWith("access_token=")) {
                accessToken = extractAccessToken(cookie)
                Log.e("Token found", accessToken.toString())
                break
            }
        }

        return response
    }

    private fun extractAccessToken(cookie: String): String {
        val tokenPart = cookie.split(";")[0]
        return tokenPart.substringAfter("access_token=")
    }
}