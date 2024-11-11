package com.example.redchat.presentation.main.chat_screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.redchat.chatPaging.ChatPager
import com.example.redchat.chatPaging.ChatPagingSource
import com.example.redchat.models.Message
import com.example.redchat.models.MessageX
import com.example.redchat.models.SendMessage
import com.example.redchat.presentation.main.MainViewModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class ChatViewmodel(private val viewModel: MainViewModel): ViewModel() {

    private var _received_message = mutableStateListOf<Message>()
    val messages: List<Message> get() = _received_message

    var message = mutableStateOf("")
    val user = viewModel.user
    val friend = viewModel.selectedFriend

    val Pager = friend?.let { ChatPager(it.conversationId) }
    val items = Pager?.getChats()?.flow?.cachedIn(viewModelScope)

    init {
        val jsonObject: JSONObject = JSONObject()
        jsonObject.put("conversationId", friend?.conversationId)
        jsonObject.put("username", user.username)
        println(jsonObject)
        viewModel.socket?.emit("joinRoom", jsonObject)
    }

    // Emit the leave room event
    fun leaveRoom(){
        val json: JSONObject = JSONObject()
        json.put("userId", viewModel.user.userId)
        viewModel.socket?.emit("leaveRoom", json)
    }

    fun listenToEvent(event: String){
        viewModel.socket?.on(event){
            viewModelScope.launch {
                var jsonObject: JSONObject = JSONObject()
                jsonObject = JSONObject(it[0].toString())
                _received_message.add(0,
                    Message(
                        content = jsonObject.getString("content"),
                        contentType = jsonObject.getString("contentType"),
                        senderId = jsonObject.getString("senderId")
                    )
                )
                println(jsonObject)
            }
        }
    }

    fun emitUserTyping(){
        val json = JSONObject()
        json.put("typing", true)
        viewModel.socket?.emit("userTyping", json)
    }

    fun sendMessage(){
        println("Send message triggered")

        // Append the new message to the end of the list
        _received_message.add(0, Message(
            contentType = "text",
            content = message.value,
            senderId = user.userId
        ))




        val sendMessage = SendMessage(
            conversationId = friend?.conversationId ?: "None",
            message = MessageX(
                content = message.value,
                contentType = "text",
                senderId = user.userId
            )
        )

        // Clear the input field
        message.value = ""

        var jsonObject: JSONObject = JSONObject()

        jsonObject = JSONObject(Gson().toJson(sendMessage))
        println(jsonObject)
        viewModel.socket?.emit("sendMessage",jsonObject)
    }

    fun stopListeningToEvent(event: String){
        viewModel.socket?.off(event)
    }

}