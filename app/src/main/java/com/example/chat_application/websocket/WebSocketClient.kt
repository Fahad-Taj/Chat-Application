package com.example.chat_application.websocket

import androidx.compose.runtime.*
import com.example.chat_application.api.RetrofitInstance
import kotlinx.coroutines.*
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

class WebSocketClient(url: String) {
    private val client = RetrofitInstance.client
    //private val client = OkHttpClient()
    private lateinit var webSocket: WebSocket
    private val request = Request.Builder().url(url).build()

    // MutableState for receiving messages
    var receivedMessage by mutableStateOf("")

    fun connect() {
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                println("Connected to WebSocket server")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                println("Received message: $text")
                // Update the state when a new message is received
                receivedMessage = text
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                println("Received ByteString message: $bytes")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                println("Closing WebSocket: $code / $reason")
                webSocket.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                println("WebSocket closed: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                println("WebSocket connection failed: ${t.message}")
            }
        })
    }

    fun sendMessage(message: String) {
        webSocket.send(message)
    }

    fun disconnect() {
        webSocket.close(1000, "Client disconnected")
    }
}