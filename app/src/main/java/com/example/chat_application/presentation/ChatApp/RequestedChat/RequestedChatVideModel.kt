package com.example.chat_application.presentation.ChatApp.RequestedChat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat_application.api.RetrofitInstance
import com.example.chat_application.models.CreateDirectChat.CreateDirectChatRequest
import com.example.chat_application.models.GetAllUsers.GetAllUsers
import com.example.chat_application.models.GetAllUsers.GetAllUsersItem
import com.example.chat_application.presentation.ChatApp.AllChat.ChatViewModel
import com.example.chat_application.util.Chat_Guid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Response

class RequestedChatVideModel : ViewModel() {

    private val _allUsers = MutableStateFlow<List<GetAllUsersItem>>(emptyList())
    val allUsers: StateFlow<List<GetAllUsersItem>> = _allUsers

    init {
        getAllUsers()
    }

    fun getAllUsers() {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.getAllUsers()
                if (result.isSuccessful) {
                    _allUsers.value = result.body() ?: emptyList()
                } else {
                    Log.e("getAllUsers", "Error: ${result.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Error in Requested", e.printStackTrace().toString())
            }
        }
    }

    fun CreateDirectChat(recipient_user_guid:CreateDirectChatRequest){
        viewModelScope.launch {
            try {
                val result=RetrofitInstance.api.createDirectChat(recipient_user_guid)
                if(result.isSuccessful){
                Chat_Guid= result.body()?.guid
                }
                else {
                    Log.e("API", "Error: ${result.errorBody()?.string()}")
                }

            }catch (e:Exception){
                Log.e("Error in CreateDirectChat", e.printStackTrace().toString())
            }
        }
    }
}