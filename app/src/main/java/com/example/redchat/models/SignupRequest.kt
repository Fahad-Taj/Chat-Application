package com.example.redchat.models

data class SignupRequest(
    val email: String,
    val password: String,
    val username: String
)