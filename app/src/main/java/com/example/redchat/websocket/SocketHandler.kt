package com.example.redchat.websocket

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URISyntaxException

object SocketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket(userId: String, username: String) {
        try {
// "http://10.0.2.2:3000" is the network your Android emulator must use to join the localhost network on your computer
// "http://localhost:3000/" will not work
// If you want to use your physical phone you could use your ip address plus :3000
// This will allow your Android Emulator and physical device at your home to connect to the server

            mSocket = IO.socket("https://redchat.azurewebsites.net?userId=66d59ee7ee65ed579f9668c9&username=fahad")
        } catch (e: URISyntaxException) {
            Log.e("SocketHandler", "Error setting socket: ${e.message}")
        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection(): Boolean {
        mSocket.connect()
        return mSocket.connected()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }
}