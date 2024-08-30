package com.example.redchat.presentation.main

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.example.redchat.api.userFromReq
import com.example.redchat.models.FriendRequest
import com.example.redchat.models.User
import com.example.redchat.models.UserDataX
import com.example.redchat.models.socket_response.receiveFriendRequest
import com.example.redchat.models.socket_response.success_error_response
import com.example.redchat.websocket.SocketHandler
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainScreenViewmodel: ViewModel() {

    lateinit var user: UserDataX
    private lateinit var socket: Socket

    init {
        // WebSocket connected once we are on the main screen
        SocketHandler.setSocket(userFromReq.userId, userFromReq.username)
        listenToSocketEvents()
        SocketHandler.establishConnection()

        println("Viewmodel initialized")

        user = UserDataX(
            friendRequests = userFromReq.friendRequests.toMutableStateList(),
            friends = userFromReq.friends,
            userId = userFromReq.userId,
            username = userFromReq.username
        )
    }

    private fun listenToSocketEvents() {
        CoroutineScope(Dispatchers.IO).launch {
            val socket = SocketHandler.getSocket()

            // Listen for the "success" event
            socket.on("success") { args ->
                // Switch to Main dispatcher for logging
                CoroutineScope(Dispatchers.Main).launch {
                    if (args.isNotEmpty()) {

                        // Approach 1: Using org.json
                        try {
                            val gson = Gson()
                            val response = gson.fromJson(args[0].toString(), success_error_response::class.java)
                            Log.d("SocketIO", "Success message: $response")
                        } catch (e: Exception) {
                            Log.e("SocketIO", "Error parsing JSON: ${e.message}")
                        }
                    } else {
                        Log.e("SocketIO", "Received data is not a String or is empty")
                    }
                }
            }


            // Listen from receiveMessage event
            socket.on("receiveFriendRequest"){ args ->

                CoroutineScope(Dispatchers.Main).launch {
                    if (args.isNotEmpty()) {

                        // Approach 1: Using org.json
                        try {
                            val gson = Gson()
                            val response = gson.fromJson(args[0].toString(), receiveFriendRequest::class.java)
                            user.friendRequests.add(
                                FriendRequest(
                                    createdAt = response.createdAt,
                                    user = User(
                                        photo = response.user.photo,
                                        username = response.user.username
                                    )
                                )
                            )
                            Log.d("Friend Requests", user.friendRequests.toString())
                            Log.d("SocketIO", "Success message: $response")
                        } catch (e: Exception) {
                            Log.e("SocketIO", "Error parsing JSON: ${e.message}")
                        }
                    } else {
                        Log.e("SocketIO", "Received data is not a String or is empty")
                    }
                }

            }

        }
    }


    override fun onCleared() {
        super.onCleared()
        // Disconnect the socket when the ViewModel is cleared
        SocketHandler.closeConnection()
    }

}