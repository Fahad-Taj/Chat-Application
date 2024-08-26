package com.example.chat_application.presentation.Authentication.login

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat_application.api.RetrofitInstance
import com.example.chat_application.models.LoginResponse
import com.example.chat_application.ui.theme.matemasie_font
import com.example.chat_application.ui.theme.opensans_font
import com.example.chat_application.ui.theme.primary_font
import com.example.chat_application.util.NetworkConnectivityChecker
import com.example.chat_application.util.access_token
import com.example.chat_application.util.user_details
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewmodel: ViewModel() {

    private val _state = mutableStateOf(LoginScreenState())
    val state: State<LoginScreenState> = _state

    // Change this later to be the LoginResponse object
    val response = MutableStateFlow<LoginResponse?>(null)

    val username = mutableStateOf("")
    val password = mutableStateOf("")

    val error_message = mutableStateOf("")

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
                val result = RetrofitInstance.api.login(username.value, password.value)
                if(result.isSuccessful){
                    access_token  = RetrofitInstance.tokenInterceptor.getSessionCookie()
                    response.value = result.body()
                    user_details = result.body()
                    error_message.value = ""
                } else {
                    Log.e("error encountered", result.message())
                    error_message.value = result.message()
                }

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