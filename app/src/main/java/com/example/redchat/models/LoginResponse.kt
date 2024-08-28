package com.example.redchat.models

data class LoginResponse(
    val message: String,
    val token: String,
    val userData: UserData
)