package com.example.redchat.models

data class SignupResponse(
    val message: String,
    val token: String,
    val userData: UserData
)