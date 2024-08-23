package com.example.chat_application.presentation.ChatApp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat_application.api.RetrofitInstance
import com.example.chat_application.models.Chat
import com.example.chat_application.models.User
import com.example.chat_application.models.UserItem
import com.example.chat_application.util.user_details
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AllChatViewModel: ViewModel() {

    val chatList = MutableStateFlow<List<Chat>>(emptyList())
    var error_message = mutableStateOf("")
    var isLoading = mutableStateOf(true)

    var selectedScreen: Chat? = null

    fun getDirectChats(){
        viewModelScope.launch {
            try {
                isLoading.value = true
                val result = RetrofitInstance.api.getDirectChats()
                chatList.value = result.body()?.chats ?: emptyList()
                isLoading.value = false

            } catch(e: Exception){
                e.printStackTrace()
        }
        }
    }



}