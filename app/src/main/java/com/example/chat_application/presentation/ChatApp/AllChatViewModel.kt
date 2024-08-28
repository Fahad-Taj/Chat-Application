package com.example.chat_application.presentation.ChatApp

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat_application.api.RetrofitInstance
import com.example.chat_application.models.Chat
import com.example.chat_application.models.LastReadMessage
import com.example.chat_application.models.Messages
import com.example.chat_application.models.OnReceived.WebSocketMessage
import com.example.chat_application.models.User
import com.example.chat_application.models.UserItem
import com.example.chat_application.util.user_details
import com.example.chat_application.util.web_socket
import com.example.chat_application.websocket.WebSocketClient
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel : ViewModel() {

    private val webSocketEcho = WebSocketClient()
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val sendMessageAdapter = moshi.adapter(SendMessageSchema::class.java)


    val receivedMessages: StateFlow<WebSocketMessage?> = webSocketEcho.receivedMessages

    val connectionStatus: StateFlow<String> = webSocketEcho.connectionStatus

    val chatList = MutableStateFlow<List<Chat>>(emptyList())

    val messageList = MutableStateFlow<List<Messages>>(emptyList())
    var error_message = mutableStateOf("")

    init {
        viewModelScope.launch {
            connectWebSocket()
            getDirectChats()

        }
//            viewModelScope.launch {
//                receivedMessages.collect { message ->
//                    when (message) {
//                        is WebSocketMessage.NewMessage -> {
//                            Log.e("NewMessage", "is typing...")
////                            addMessageToList(message)
//                        }
//                        is WebSocketMessage.UserTyping -> {
//                            // Handle user typing logic
//                            Log.e("UserTyping", "${message.user_guid} is typing...")
//                        }
//                        is WebSocketMessage.StatusMessage -> {
//                            // Handle status update logic
//                            Log.e("StatusMessage", "Status: ${message.status}")
//                        }
//                        is WebSocketMessage.MessageRead -> {
//                            // Handle message read logic
//                            Log.e("MessageRead", "Message read by ${message.user_guid}")
//                        }
//                        else -> {}
//                    }
//                }
//            }
    }
    var isLoading = mutableStateOf(true)


    var selectedScreen: Chat? = null
    var msg = mutableStateOf("")

    private val _messages = MutableStateFlow(
        Messages(
            has_more_messages = false,
            last_read_message = LastReadMessage("", "", ""),
            messages = mutableStateListOf()
        )
    )

    val messages: StateFlow<Messages> = _messages.asStateFlow()

    //connect to a web socket
    fun connectWebSocket() {
        viewModelScope.launch {
            webSocketEcho.connect(web_socket)
        }
    }


    //get previous chats from pagination
    fun getMessages(chatGuid: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                Log.e("chat guid", chatGuid)
                val response = RetrofitInstance.api.getMessages(chatGuid, 20)
                if (response.isSuccessful) {
                    _messages.value = response.body() ?: _messages.value
                } else {
                    Log.e("getMessages", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("getMessages", "Exception: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }

    fun disconnectWebSocket() {
        viewModelScope.launch {
            webSocketEcho.disconnect()
        }
    }


    //chat details we can get it from here like
    fun getDirectChats() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val result = RetrofitInstance.api.getDirectChats()
                chatList.value = result.body()?.chats ?: emptyList()
                isLoading.value = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //send the message to
    fun sendMessage(message: String, userGuid: String, chatGuid: String) {
        viewModelScope.launch {
            val newMessage = SendMessageSchema(
                user_guid = userGuid, chat_guid = chatGuid, content = message
            )
            val jsonMessage = sendMessageAdapter.toJson(newMessage)
            Log.e("WebSocket", "Sending message: $jsonMessage")
            val success = webSocketEcho.sendMessage(jsonMessage)
            if (success) {
                msg.value = ""

            } else {
                updateText(msg.value)
                Log.e("WebSocket", "Failed to send message")
            }
        }
    }

    //update the text each time
    fun updateText(text: String) {
        msg.value = text
    }


}

@JsonClass(generateAdapter = true)
data class SendMessageSchema(
    val type: String = "new_message",
    val user_guid: String,
    val chat_guid: String,
    val content: String,
)
