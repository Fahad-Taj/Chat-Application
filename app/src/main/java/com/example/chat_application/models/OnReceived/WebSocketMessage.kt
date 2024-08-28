package com.example.chat_application.models.OnReceived

sealed class WebSocketMessage {
    data class MessageRead(
        val chat_guid: String,
        val last_read_message_created_at: String,
        val last_read_message_guid: String,
        val type: String,
        val user_guid: String
    ):WebSocketMessage()

    data class NewMessage(
        val chat_guid: String,
        val content: String,
        val created_at: String,
        val is_read: Boolean,
        val message_guid: String,
        val type: String,
        val user_guid: String
    ):WebSocketMessage()

    data class StatusMessage(
        val status: String,
        val type: String,
        val user_guid: String,
        val username: String
    ):WebSocketMessage()

    data class UserTyping(
        val chat_guid: String,
        val type: String,
        val user_guid: String
    ):WebSocketMessage()
}