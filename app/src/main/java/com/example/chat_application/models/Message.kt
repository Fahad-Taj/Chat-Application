package com.example.chat_application.models

data class Message(
    val chat_guid: String,
    val content: String,
    val created_at: String,
    val is_read: Boolean,
    val message_guid: String,
    val user_guid: String
)