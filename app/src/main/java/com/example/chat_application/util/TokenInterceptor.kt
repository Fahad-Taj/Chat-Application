package com.example.chat_application.util

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
                .header("cookie", "access_token=$accessToken")
                .build()
        } else {
            originalRequest
        }

        val response = chain.proceed(newRequest)

        // Extract the token from incoming responses
        val cookies = response.headers("set-cookie")
        for (cookie in cookies) {
            if (cookie.startsWith("access_token=")) {
                accessToken = extractAccessToken(cookie)
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