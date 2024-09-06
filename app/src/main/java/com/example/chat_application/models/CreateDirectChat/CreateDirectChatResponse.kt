package com.example.chat_application.models.CreateDirectChat

data class CreateDirectChatResponse(
    val chat_type: String,
    val created_at: String,
    val guid: String,
    val updated_at: String,
    val users: List<User>
)