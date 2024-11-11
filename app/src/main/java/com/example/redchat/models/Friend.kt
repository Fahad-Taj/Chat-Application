package com.example.redchat.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

data class Friend(
    val UID: String,
    val conversationId: String,
    val photo: String?,
    val username: String,
    var isOnline: Boolean = false,
    var isTyping: Boolean = false
)