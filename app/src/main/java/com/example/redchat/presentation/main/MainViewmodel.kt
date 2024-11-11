package com.example.redchat.presentation.main

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redchat.api.userFromReq
import com.example.redchat.models.Friend
import com.example.redchat.models.FriendRequest
import com.example.redchat.models.FriendStatus
import com.example.redchat.models.User
import com.example.redchat.models.UserData
import com.example.redchat.models.UserDataX
import com.example.redchat.models.socket_response.receiveFriendRequest
import com.example.redchat.models.socket_response.success_error_response
import com.example.redchat.websocket.SocketHandler
import com.example.redchat.websocket.SocketHandler.mSocket
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URISyntaxException

class MainViewModel : ViewModel() {

    var selectedFriend: Friend? = null
    var socket: Socket? = null
    private val _socketEvents = MutableStateFlow<SocketEvent?>(null)
    val socketEvents = _socketEvents.asStateFlow()

    lateinit var user: UserData

    init {
        println("Viewmodel initialized")
        setUser()
        viewModelScope.launch {
            initializeSocket()
        }
    }

    private fun setUser(){
        user = UserData(
            userId = userFromReq.userId,
            username = userFromReq.username,
            friendRequests = userFromReq.friendRequests.toMutableStateList(),
            friends = userFromReq.friends.toMutableStateList()
        )
    }

    private suspend fun initializeSocket() {
        viewModelScope.launch {
            try{
                socket = IO.socket("https://redchat.azurewebsites.net?userId=${user.userId}&username=${user.username}")
                println(socket.toString())
                socket?.apply {
                    on(Socket.EVENT_CONNECT){
                        println("Socket connected")
                        _socketEvents.value = SocketEvent.Connected
                    }
                    on(Socket.EVENT_DISCONNECT){
                        println("Socket Disconnected")
                        _socketEvents.value = SocketEvent.DisConnected
                    }
                    on("success"){ args->
                        val jsonObject = JSONObject(args[0].toString())
                        _socketEvents.value = SocketEvent.Success
                        Log.e("Success event", jsonObject.toString())
                        println(args.toString())
                    }
                    on("error"){ args->
                        val jsonObject = JSONObject(args[0].toString())
                        _socketEvents.value = SocketEvent.Success
                        Log.e("Error event", jsonObject.toString())
                        println(args.toString())
                    }
                    on("receiveFriendRequest"){
                        println("Friend request received, $it" )
                        _socketEvents.value = SocketEvent.ReceiveFriendRequest
                        val gson = Gson()
                        val data = gson.fromJson(it[0].toString(), receiveFriendRequest::class.java)
                        val friendRequest = FriendRequest(
                            createdAt = data.createdAt,
                            user = User(
                                photo = data.user.photo,
                                username = data.user.username
                            )
                        )
                        user.friendRequests.add(friendRequest)

                    }
                    on("acceptedFriendRequest"){
                        println("Friend request accepted, $it" )
                        val gson = Gson()
                        val friend = gson.fromJson(it[0].toString(), Friend::class.java)
                        user.friends.add(friend)
                    }
                    on("friendStatus") { payload ->
                        println("Friend status changed: $payload")
                        try {
                            val gson = Gson()
                            val friendStatus = gson.fromJson(payload[0].toString(), FriendStatus::class.java)
                            println("Parsed friend status: $friendStatus")

                            user.friends.find { it.UID == friendStatus.userId }?.let { friend ->
                                println(friendStatus.online::class)
                                friend.isOnline = (friendStatus.online)
                                println("Updated online status for user ${friendStatus.userId} to ${friendStatus.online}")
                            } ?: println("Friend with ID ${friendStatus.userId} not found")
                        } catch (e: Exception) {
                            println("Error parsing friend status: ${e.message}")
                        }
                    }



                    // Connect the socket
                    connect()

                    // Check if the socket is connected
                    if (connected()) {
                        Log.d("Socket", "Socket connection established")
                    } else {
                        Log.e("Socket", "Socket connection not established")
                    }

                }
            } catch(e: URISyntaxException){
                e.printStackTrace()
            }
        }
    }

    fun emitEvent(event: String, data: Any){
        socket?.emit(event, data)
    }

    override fun onCleared() {
        super.onCleared()
        socket?.disconnect()
        socket?.off()
    }
}

sealed class SocketEvent(){
    object Connected: SocketEvent()
    object DisConnected: SocketEvent()
    object Success: SocketEvent()
    object Failure: SocketEvent()
    object ReceiveFriendRequest: SocketEvent()
}