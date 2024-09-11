package com.example.redchat.presentation.authentication.signup

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
import com.example.redchat.models.SignupRequest
import com.example.redchat.models.SignupResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignupViewmodel: ViewModel() {

    val _response = MutableStateFlow<SignupResponse?>(null)
    val response = _response.asStateFlow()

    var email = mutableStateOf("")
    var username = mutableStateOf("")
    var password = mutableStateOf("")

    var isLoading = mutableStateOf(false)

    fun signup(context: Context){
        viewModelScope.launch {
            try {
                isLoading.value = true
                val signupRequest = SignupRequest(
                    email = email.value,
                    username = username.value,
                    password = password.value
                )
                val result = RetrofitInstance.api.signup(signupRequest)
                if(result.isSuccessful){
                    access_token = result.body()?.token
                    _response.value = result.body()
                    userFromReq = result.body()?.userData!!
                    Toast.makeText(context, "User created successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, result.code().toString(), Toast.LENGTH_SHORT).show()
                }
                isLoading.value = false

            } catch(e: Exception){
                isLoading.value = false
                Toast.makeText(context, "Failed to create user", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

}