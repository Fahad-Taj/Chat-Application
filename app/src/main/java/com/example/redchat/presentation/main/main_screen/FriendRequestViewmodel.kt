package com.example.redchat.presentation.main.main_screen

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redchat.api.RetrofitInstance
import com.example.redchat.api.access_token
import com.example.redchat.models.Data
import com.example.redchat.models.Friend
import com.example.redchat.models.SendFriendRequestRequest
import com.example.redchat.presentation.main.MainViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject

class FriendRequestViewmodel(viewModel: MainViewModel): ViewModel() {

    var isLoading = mutableStateOf(false)

    // Function to accept a friend request
    fun acceptFriendRequest(viewModel: MainViewModel, username: String, context: Context){
        if(!isLoading.value){
            viewModelScope.launch {
                try {
                    isLoading.value = true
                    val bearerToken = "Bearer $access_token"
                    val result = RetrofitInstance.api.acceptFriendRequest(bearerToken,SendFriendRequestRequest(username = username))
                    if(result.isSuccessful){
                        val friend = result.body()?.data?.let {
                            Friend(
                                UID = it.UID,
                                conversationId = it.conversationId,
                                username = it.username,
                                photo = it.photo
                            )
                        }
                        viewModel.user.friends.add(friend!!)
                        viewModel.user.friendRequests.removeIf { it.user.username == username }
                        Toast.makeText(context, "Friend request accepted", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorObj = JSONObject(result.errorBody()!!.charStream().readText())
                        val errorString = errorObj.getString("message")
                        Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
                    }
                    isLoading.value = false
                } catch(e: Exception){
                    isLoading.value = false
                    e.printStackTrace()
                }
            }
        }
    }

    // Function to reject a friend request
    fun rejectFriendRequest(viewModel: MainViewModel, username: String, context: Context){
        if(!isLoading.value){
            viewModelScope.launch {
                try {
                    isLoading.value = true
                    val bearerToken = "Bearer $access_token"
                    val result = RetrofitInstance.api.rejectFriendRequest(bearerToken, username)
                    if(result.isSuccessful){
                        viewModel.user.friendRequests.removeIf { it.user.username == username }
                        Toast.makeText(context, "Friend request rejected", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorObj = JSONObject(result.errorBody()!!.charStream().readText())
                        val errorString = errorObj.getString("message")
                        Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
                    }
                    isLoading.value = false
                } catch(e: Exception){
                    isLoading.value = false
                    e.printStackTrace()
                }
            }
        }
    }

}