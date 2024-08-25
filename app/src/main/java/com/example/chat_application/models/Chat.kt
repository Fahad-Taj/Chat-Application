package com.example.chat_application.models

import java.io.Serializable

data class Chat(
    val chat_guid: String,
    val chat_type: String,
    val created_at: String,
    val new_messages_count: Int,
    val updated_at: String,
    val users: List<User>
)