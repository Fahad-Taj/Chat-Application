package com.example.redchat.models.socket_response

data class receiveMessage(
    val content: String,
    val contentType: String,
    val senderId: String
)