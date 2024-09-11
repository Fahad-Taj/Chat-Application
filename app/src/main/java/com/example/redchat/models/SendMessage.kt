package com.example.redchat.models

data class SendMessage(
    val conversationId: String,
    val message: MessageX
)