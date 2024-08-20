package com.example.chat_application.presentation.Authentication.login

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat_application.ui.theme.matemasie_font
import com.example.chat_application.ui.theme.opensans_font
import com.example.chat_application.ui.theme.primary_font
import com.example.chat_application.util.NetworkConnectivityChecker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewmodel: ViewModel() {

    private val _state = mutableStateOf(LoginScreenState())
    val state: State<LoginScreenState> = _state

    // Change this later to be the LoginResponse object
    val response = MutableStateFlow<String?>(null)

    val username = mutableStateOf("")
    val password = mutableStateOf("")

    var isWaiting = mutableStateOf(false)

    fun checkInternetConnection(context: Context, lifecycleOwner: LifecycleOwner){
        viewModelScope.launch {
            val networkConnectivityChecker = NetworkConnectivityChecker(context)
            networkConnectivityChecker.observe(lifecycleOwner) {
                _state.value = state.value.copy(
                    hasInternetConnection = it
                )
            }
        }
    }

    fun login(){
        viewModelScope.launch {
            try {
                isWaiting.value = true
                // delay to simulate network request
                delay(1000)
                response.value = "Success"


                isWaiting.value = false
            } catch(e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun change_font(){
        if (primary_font.value == matemasie_font){
            primary_font.value = opensans_font
        } else {
            primary_font.value = matemasie_font
        }
    }

}