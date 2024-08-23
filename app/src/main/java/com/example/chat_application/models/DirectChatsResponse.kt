package com.example.chat_application.models

data class DirectChatsResponse(
    val chats: List<Chat>,
    val total_unread_messages_count: Int
)