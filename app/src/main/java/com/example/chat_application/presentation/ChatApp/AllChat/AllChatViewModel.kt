package com.example.chat_application.presentation.ChatApp.AllChat

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat_application.api.RetrofitInstance
import com.example.chat_application.models.Chat
import com.example.chat_application.models.LastReadMessage
import com.example.chat_application.models.Message
import com.example.chat_application.models.Messages
import com.example.chat_application.models.OnReceived.WebSocketMessage
import com.example.chat_application.util.Chat_Guid
import com.example.chat_application.util.web_socket
import com.example.chat_application.websocket.WebSocketClient
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(DelicateCoroutinesApi::class)
class ChatViewModel : ViewModel() {

    private val webSocketEcho = WebSocketClient()
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val sendMessageAdapter = moshi.adapter(SendMessage_Schema::class.java)

    private val _messages = MutableStateFlow(
        Messages(
            has_more_messages = false,
            last_read_message = LastReadMessage("", "", ""),
            messages = mutableListOf()))

    val messages: StateFlow<Messages> = _messages


    val receivedMessages: StateFlow<WebSocketMessage?> = webSocketEcho.receivedMessages
    val connectionStatus: StateFlow<String> = webSocketEcho.connectionStatus


    //list of different chat that user is friend with something like that
    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats


    val isTyping = MutableStateFlow<Boolean>(false)
    val isStatus = MutableStateFlow<Boolean>(false)


    init {
        connectWebSocket()
        viewModelScope.launch {
            receivedMessages.collect { message ->
                when (message) {
                    is WebSocketMessage.NewMessage -> {
                        Log.e("NewMessage", "Message added: $message")
                        val newMessage: Message = NewMessagetoMessage(message)

                        if (message.chat_guid == Chat_Guid) {
                            // Access the current list of messages
                            val currentMessages = _messages.value.messages.toMutableList()
                            // Add the new message to the list
                            currentMessages.add(newMessage)
                            // Update the StateFlow with the new list of messages
                            _messages.value = _messages.value.copy(messages = currentMessages)
                            Log.e("NewMessage", "Message added: ${_messages.value.messages.last()}")
                        }
                    }

                    is WebSocketMessage.UserTyping -> {
                        // Handle user typing logic
                        if (message.type == "user_typing") {
                            isTyping.value = true
                            GlobalScope.launch {
                                delay(2500) // Delay for 2 second
                                isTyping.value = false
                            }
                        } else {
                            isTyping.value = false
                        }
                        Log.e("UserTyping", "${message.user_guid} is typing...")
                    }

                    is WebSocketMessage.StatusMessage -> {
                        // Handle status update logic
                        if (message.status == "online") {
                            isStatus.value = true
                        } else {
                            isStatus.value = false
                        }
                        Log.e("StatusMessage", "Status: ${message.status}")
                    }

                    is WebSocketMessage.MessageRead -> {
                        // Handle message read logic
                        //somehow need to save the last_read_message or like that where
                        Log.e("MessageRead", "Message read by ${message.last_read_message_guid}")
                    }

                    else -> {}
                }
            }
        }
    }




    var isLoading = mutableStateOf(true)


    var selectedScreen: Chat? = null
    var msg = mutableStateOf("")


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
                _chats.value = result.body()?.chats ?: emptyList()
                isLoading.value = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //send the message to
    fun sendMessage(message: String, userGuid: String, chatGuid: String) {
        viewModelScope.launch {
            val newMessage = SendMessage_Schema(
                user_guid = userGuid, chat_guid = chatGuid, content = message
            )
            val jsonMessage = sendMessageAdapter.toJson(newMessage)
            Log.e("WebSocket", "Sending message: $jsonMessage")
            val success = webSocketEcho.sendMessage(jsonMessage)
            if (success) {
                msg.value = ""
            } else {
                Log.e("WebSocket", "Failed to send message")
            }
        }
    }



}

@JsonClass(generateAdapter = true)
data class SendMessage_Schema(
    val type: String = "new_message",
    val user_guid: String,
    val chat_guid: String,
    val content: String,
)
fun NewMessagetoMessage(message: WebSocketMessage.NewMessage) :Message{
    return Message(
        user_guid = message.user_guid,
        chat_guid = message.chat_guid,
        content = message.content,
        created_at = message.created_at,
        is_read = message.is_read,
        message_guid =message.message_guid
    )
}