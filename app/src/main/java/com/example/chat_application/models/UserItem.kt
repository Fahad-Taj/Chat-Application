package com.example.chat_application.models

data class UserItem(
    val created_at: String,
    val email: String,
    val first_name: String,
    val guid: String,
    val last_name: String,
    val user_image: Any,
    val username: String
)