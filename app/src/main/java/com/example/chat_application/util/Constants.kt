package com.example.chat_application.util

import com.example.chat_application.models.LoginResponse

val baseUrl = "http://192.168.0.100:8001"
var access_token: String? = null
val refresh_token: String? = null
val web_socket: String = "ws://192.168.0.100:8001/ws/"
var user_details: LoginResponse? = null

var User_Guid: String? = null
var Chat_Guid: String? = null
var Unread_Message_Count=0;



//fahad
// Add the delete command and go the back screen if it is successful
// list all the chats registered user and on click on any it establishes the condition
// and we got to the chat and open it
//set the navigation in an correct way

//Questions
//what are delete response
// what to read
// issue with the date
