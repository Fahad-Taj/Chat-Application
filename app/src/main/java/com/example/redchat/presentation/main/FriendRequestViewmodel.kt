package com.example.redchat.presentation.main

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redchat.api.RetrofitInstance
import com.example.redchat.api.access_token
import com.example.redchat.models.SendFriendRequestRequest
import kotlinx.coroutines.launch
import org.json.JSONObject

class FriendRequestViewmodel: ViewModel() {

    var isLoading = mutableStateOf(false)

    // Function to accept a friend request
    fun acceptFriendRequest(viewModel: MainScreenViewmodel,username: String, context: Context){
        if(!isLoading.value){
            viewModelScope.launch {
                try {
                    isLoading.value = true
                    val bearerToken = "Bearer $access_token"
                    val result = RetrofitInstance.api.acceptFriendRequest(bearerToken,SendFriendRequestRequest(username = username))
                    if(result.isSuccessful){
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
    fun rejectFriendRequest(viewModel: MainScreenViewmodel,username: String, context: Context){
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