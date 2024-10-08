package com.example.chat_application.presentation.ChatApp.AllChat

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.chat_application.api.RetrofitInstance
import com.example.chat_application.models.Chat
import com.example.chat_application.models.CreateDirectChat.CreateDirectChatRequest
import com.example.chat_application.models.DirectChatsResponse
import com.example.chat_application.models.LastReadMessage
import com.example.chat_application.models.Message
import com.example.chat_application.models.Messages
import com.example.chat_application.models.OnReceived.WebSocketMessage
import com.example.chat_application.presentation.ChatApp.ChatRoutes
import com.example.chat_application.util.Chat_Guid
import com.example.chat_application.util.User_Guid
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
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

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
            messages = mutableListOf()
        )
    )

    val messages: StateFlow<Messages> = _messages


    val receivedMessages: StateFlow<WebSocketMessage?> = webSocketEcho.receivedMessages
    val connectionStatus: StateFlow<String> = webSocketEcho.connectionStatus


    //list of different chat that user is friend with something like that

    private val _chats = MutableStateFlow<Response<DirectChatsResponse>?>(null)
    val chats: StateFlow<Response<DirectChatsResponse>?> = _chats


    private val _isTyping = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val isTyping: StateFlow<Map<String, Boolean>> = _isTyping

    private val _isStatus = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val isStatus: StateFlow<Map<String, Boolean>> = _isStatus


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
                            _isTyping.value = _isTyping.value.toMutableMap().apply {
                                put(message.user_guid, true)
                            }
                            GlobalScope.launch {
                                delay(3000) // Delay for 3 second
                                _isTyping.value = _isTyping.value.toMutableMap().apply {
                                    put(message.user_guid, false)
                                }
                            }
                        } else {
                            _isTyping.value = _isTyping.value.toMutableMap().apply {
                                put(message.user_guid, false)
                            }
                        }
                        Log.e("UserTyping", "${message.user_guid} is typing...")
                    }

                    is WebSocketMessage.StatusMessage -> {
                        // Handle status update logic
                        if (message.status == "online") {
                            _isStatus.value = _isStatus.value.toMutableMap().apply {
                                put(message.user_guid, true)
                            }
                        } else {
                            _isStatus.value = _isStatus.value.toMutableMap().apply {
                                put(message.user_guid, false)
                            }
                        }
                        Log.e("StatusMessage", "Status: ${message.status}")
                    }

                    is WebSocketMessage.MessageRead -> {
                        viewModelScope.launch {
                            val updatedMessages = _messages.value.messages.map { msg ->
                                // Check if the message matches chat_guid and user_guid
                                if (msg.chat_guid == message.chat_guid && msg.message_guid == message.last_read_message_guid && msg.user_guid == message.user_guid) {
                                    // Only update is_read field
                                    msg.copy(is_read = true)
                                } else {
                                    msg
                                }
                            }
                            _messages.value =
                                _messages.value.copy(messages = updatedMessages.toMutableList())
                            Log.e("check me", _messages.value.toString())
                        }


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
                    Log.e("GetMessage", response.body().toString())

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

    fun sendMessageReadEvent(
        chatGuid: String, messageGuid: String
    ) {
        val messageReadEvent = JSONObject().apply {
            put("type", "message_read")
            put("chat_guid", chatGuid)
            put("message_guid", messageGuid)
        }
        webSocketEcho.sendMessage(messageReadEvent.toString())
    }


    //chat details we can get it from here like
    fun getDirectChats() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val result = RetrofitInstance.api.getDirectChats()
                _chats.value = result
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


    fun createDirectChat(
        navController: NavHostController,
        recipient_user_guid:String
    ){
        viewModelScope.launch {
            try{
                val request = CreateDirectChatRequest(recipient_user_guid)
                val response = RetrofitInstance.api.createDirectChat(request)
                if(response.isSuccessful){
                    val response_body = response.body()
                    val chat = Chat(
                        chat_guid = response_body?.guid!!,
                        chat_type = response_body.chat_type,
                        created_at = response_body.created_at,
                        new_messages_count = 0,
                        updated_at = response_body.updated_at,
                        users = response_body.users
                    )
                    selectedScreen = chat
                    navController.navigate(ChatRoutes.ChatScreen.route)

                }
            }   catch(e: Exception){
                e.printStackTrace()
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

fun NewMessagetoMessage(message: WebSocketMessage.NewMessage): Message {
    return Message(
        user_guid = message.user_guid,
        chat_guid = message.chat_guid,
        content = message.content,
        created_at = message.created_at,
        is_read = message.is_read,
        message_guid = message.message_guid
    )
}

data class MessageReadEvent(
    val type: String,
    val user_guid: String,
    val chat_guid: String,
    val last_read_message_guid: String,
    val last_read_message_created_at: String
)