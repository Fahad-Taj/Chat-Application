package com.example.chat_application.models

data class LastReadMessage(
    val content: String,
    val created_at: String,
    val guid: String
)