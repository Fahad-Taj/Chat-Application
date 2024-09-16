package com.example.chat_application.presentation.Authentication.signup

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat_application.api.RetrofitInstance
import com.example.chat_application.models.RegisterResponse
import com.example.chat_application.presentation.Authentication.login.LoginScreenState
import com.example.chat_application.util.NetworkConnectivityChecker
import com.example.chat_application.util.access_token
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class SignupViewmodel: ViewModel() {

    val response = MutableStateFlow<String?>(null)

    private val _state = mutableStateOf(LoginScreenState())
    val state: State<LoginScreenState> = _state

    val error_message = mutableStateOf("")

    val username = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val firstName = mutableStateOf("")
    val lastName = mutableStateOf("")

    var isWaiting = mutableStateOf(false)

    var uri = mutableStateOf<Uri?>(null)

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

    fun register(){
        viewModelScope.launch {
            try {
                isWaiting.value = true

                val result = RetrofitInstance.api.register(
                    username = username.value,
                    password = password.value,
                    first_name = firstName.value,
                    last_name = lastName.value,
                    email = email.value
                )
                if(result.isSuccessful){
                    access_token=RetrofitInstance.tokenInterceptor.getSessionCookie()
                    response.value = result.body()
                    error_message.value = ""
                } else {
                    Log.e("error encountered", result.message())
                    error_message.value = result.message()
                }


                isWaiting.value = false
            } catch(e: Exception){
                isWaiting.value = false
                e.printStackTrace()
            }
        }
    }

    fun uriToFile(uri: Uri, context: Context): File? {
        val contentResolver = context.contentResolver
        val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(tempFile)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return tempFile
    }

}