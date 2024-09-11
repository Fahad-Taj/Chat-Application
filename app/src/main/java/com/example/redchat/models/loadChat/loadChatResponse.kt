package com.example.redchat.models.loadChat

data class loadChatResponse(
    val hasMore: Boolean,
    val messages: List<Message>,
    val totalMessages: Int
)