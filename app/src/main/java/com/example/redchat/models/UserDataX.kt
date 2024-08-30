package com.example.redchat.models

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class UserDataX(
    val friendRequests: SnapshotStateList<FriendRequest> = mutableStateListOf(),
    val friends: List<Friend>,
    val userId: String,
    val username: String
)
