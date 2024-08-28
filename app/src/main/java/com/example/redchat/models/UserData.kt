package com.example.redchat.models

data class UserData(
    val friendRequests: List<Any>,
    val friends: List<Any>,
    val userId: String,
    val username: String
)