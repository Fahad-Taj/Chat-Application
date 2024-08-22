package com.example.chat_application.models

data class LoginResponse(
    val email: String,
    val first_name: String,
    val last_name: String,
    val settings: Settings,
    val user_guid: String,
    val user_image: String?,
    val username: String
)