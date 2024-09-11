package com.example.redchat.models

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class UserData(
    val friendRequests: SnapshotStateList<FriendRequest> = mutableStateListOf(),
    val friends: SnapshotStateList<Friend> = mutableStateListOf(),
    val userId: String,
    val username: String
)