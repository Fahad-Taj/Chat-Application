package com.example.redchat.presentation.authentication.login

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redchat.api.RetrofitInstance
import com.example.redchat.api.access_token
import com.example.redchat.api.userFromReq
import com.example.redchat.models.LoginRequest
import com.example.redchat.models.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewmodel: ViewModel() {

    var username = mutableStateOf("")
    var password = mutableStateOf("")

    val _response = MutableStateFlow<LoginResponse?>(null)
    val response = _response.asStateFlow()

    var isLoading = mutableStateOf(false)

    fun login(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading.value = true
                val loginRequest = LoginRequest(
                    username = username.value,
                    password = password.value
                )
                val result = RetrofitInstance.api.login(loginRequest)
                if(result.isSuccessful){
                    access_token = result.body()?.token
                    _response.value = result.body()
                    userFromReq = result.body()?.userData!!
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, result.code().toString(), Toast.LENGTH_SHORT).show()
                }
                isLoading.value = false

            } catch(e: Exception){
                isLoading.value = false
                e.printStackTrace()
            }
        }
    }

}