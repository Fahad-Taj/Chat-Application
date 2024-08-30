package com.example.redchat.models

data class UserData(
    val friendRequests: List<FriendRequest>,
    val friends: List<Friend>,
    val userId: String,
    val username: String
)