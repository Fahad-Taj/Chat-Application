package com.example.redchat.models

data class Friend(
    val UID: String,
    val conversationId: String,
    val photo: String?,
    val username: String
)