package com.example.chat_application.presentation.ChatApp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat_application.api.RetrofitInstance
import com.example.chat_application.models.UserItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AllChatViewModel: ViewModel() {

    val userList = MutableStateFlow<List<UserItem>>(emptyList())
    var error_message = mutableStateOf("")
    var isLoading = mutableStateOf(true)

    fun get_users(){
        viewModelScope.launch {
            try {
                isLoading.value = true
                val result = RetrofitInstance.api.getUsers()
                if(result.isSuccessful){
                    userList.value = result.body()!!
                } else {
                    error_message.value = result.message()
                }
                isLoading.value = false
            } catch(e: Exception){
                e.printStackTrace()
            }
        }
    }

}