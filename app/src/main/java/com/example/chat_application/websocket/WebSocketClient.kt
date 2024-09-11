package com.example.chat_application.websocket

import android.util.Log
import com.example.chat_application.api.RetrofitInstance
import com.example.chat_application.models.OnReceived.WebSocketMessage
import com.example.chat_application.util.web_socket
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.*
import okio.ByteString

// To connect to a websocket in android, we have to track 4 entities
// WebSocketClient - This is a completely custom class you can name it anything, this class is your primary interface for interacting with OkHttp websockets
//                   This class contains 3 objects they are as follows

// client - This is our primary OkHttp client, this object gives us the functionalities of the OkHttp class, this object is used to create a websocket
//          and send requests. Very powerful object

// request - This is the request object, it will contain the information for making a websocket connection, contains url and all

// Here comes the main part
// websocket = client.newWebSocket(request: Request, objectL WebSocketListener)() {
//  ..........
// }
// We are initializing the webSocket object of type WebSocket - we are using client's methods to create a websocket connection by passing the
// request object and creating an object of the WebSocketListener interface, we are basically implementing the WebSocketListener interface by overriding
// it's functions and an object of this new implemented class is our webSocket object.

// All four methods have now been implemented, we can now send messages using the webSocket.send() method, we can close the connection by using the
// webSocket.close() methods, this method takes two parameters, one is the code, and the reason for closing

// Now we can use the WebSocketClient class' objects to create connection, we will use WebSocketClient.sendMessage() and WebSocketClient.disconnect()
// methods to manage the webSocket connection

class WebSocketClient() {
    private val client = RetrofitInstance.client
    private lateinit var webSocket: WebSocket
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()


    private val _receivedMessages = MutableStateFlow<WebSocketMessage?>(null)
    val receivedMessages: StateFlow<WebSocketMessage?> = _receivedMessages


    private val _connectionStatus = MutableStateFlow<String>("disconnected")
    val connectionStatus: StateFlow<String>  = _connectionStatus

    fun connect(url: String) {

        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.e("OnOpen", "Connected to WebSocket server")
                _connectionStatus.value = "connected"
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.e("onMessage", "Received message: $text")
                val jsonAdapter = moshi.adapter(Map::class.java)
                val messageMap = jsonAdapter.fromJson(text)!!
                when (messageMap["type"]) {
                    "new" -> {
                        val newMessage =
                            moshi.adapter(WebSocketMessage.NewMessage::class.java).fromJson(text)!!
                        _receivedMessages.value = newMessage
                        Log.e("newreceivedMessage", _receivedMessages.value.toString())
                    }

                    "user_typing" -> {
                        val userTyping =
                            moshi.adapter(WebSocketMessage.UserTyping::class.java).fromJson(text)!!
                        _receivedMessages.value = userTyping
                        Log.e("userreceivedMessage", _receivedMessages.value.toString())
                    }

                    "status" -> {
                        val status = moshi.adapter(WebSocketMessage.StatusMessage::class.java)
                            .fromJson(text)!!
                        _receivedMessages.value = status
                        Log.e("statusreceivedMessage", _receivedMessages.value.toString())
                    }

                    "message_read" -> {
                        val message_read =
                            moshi.adapter(WebSocketMessage.MessageRead::class.java).fromJson(text)!!
                        _receivedMessages.value = message_read
                        Log.e("readreceivedMessage", _receivedMessages.value.toString())
                    }
                }
//                Log.e("Received",_receivedMessages.value)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Log.e("OnMessage", "Received ByteString message: $bytes")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.e("onClosing", "Closing WebSocket: $code / $reason")
                webSocket.close(1000, null)
                _connectionStatus.value = "disconnected"
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.e("onClosed", "WebSocket closed: $code / $reason")
                _connectionStatus.value = "disconnected"
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("onFailure", "WebSocket connection failed: ${t.message}")
                _connectionStatus.value = "disconnected"
                reconnect()
            }
        })
    }

    fun sendMessage(message: String): Boolean {
        return try {
            webSocket.send(message) == true
            //also update the msg here
        } catch (e: Exception) {
            Log.e("WebSocketClient", "Error sending message: ${e.message}")
            false
        }
    }

    fun disconnect() {
        if (::webSocket.isInitialized) {
            webSocket.close(1000, "Client disconnected")
            _connectionStatus.value = "disconnected"
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun reconnect() {
        GlobalScope.launch {
            delay(2000) // Delay for 2 seconds before attempting to reconnect
            connect(web_socket)
        }
    }
}
