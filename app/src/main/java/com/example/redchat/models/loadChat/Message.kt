package com.example.redchat.models.loadChat

data class Message(
    val _id: String,
    val content: String,
    val contentLink: Any,
    val contentType: String,
    val senderId: String,
    val timestamp: String
)