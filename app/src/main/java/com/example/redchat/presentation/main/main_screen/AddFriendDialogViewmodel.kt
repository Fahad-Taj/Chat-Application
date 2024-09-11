package com.example.redchat.presentation.main.main_screen

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redchat.api.RetrofitInstance
import com.example.redchat.api.access_token
import com.example.redchat.models.SendFriendRequestRequest
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class AddFriendDialogViewmodel: ViewModel() {
    var username by mutableStateOf("")
    var isLoading = mutableStateOf(false)

    fun sendFriendRequest(context: Context){
        viewModelScope.launch {
            try {
                isLoading.value = true
                val bearer_token = "Bearer $access_token"
                val sendFriendRequestRequest = SendFriendRequestRequest(username)
                val result = RetrofitInstance.api.sendFriendRequest(bearer_token, sendFriendRequestRequest)
                println(result)

                if(result.code() == 200){
                    Toast.makeText(context, "Friend request sent", Toast.LENGTH_SHORT).show()
                } else {
                    val errorObj = JSONObject(result.errorBody()!!.charStream().readText())
                    val errorString = errorObj.getString("message")
                    Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
                }

            } catch (e: HttpException) {
                // Handle HTTP exception
                val errorResponse = e.response()?.errorBody()?.string()
                Toast.makeText(context, errorResponse, Toast.LENGTH_SHORT).show()
            }  catch (e: Exception){
                Toast.makeText(context, "Can't send request", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
}