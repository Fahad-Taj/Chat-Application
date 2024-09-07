package com.example.chat_application.models

data class Messages(
    val has_more_messages: Boolean,
    val last_read_message: LastReadMessage?,
    val messages: MutableList<Message>
)