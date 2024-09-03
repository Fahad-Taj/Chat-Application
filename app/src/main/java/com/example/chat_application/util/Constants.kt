package com.example.chat_application.util

import com.example.chat_application.models.LoginResponse

val baseUrl = "http://192.168.0.101:8001"
var access_token: String? = null
val refresh_token: String? = null
val web_socket: String = "ws://192.168.0.101:8001/ws/"
var user_details: LoginResponse? = null

var User_Guid: String? = null
var Chat_Guid: String? = null


//Pending the works:-
//when I get the list after that what I can do is that after
// click it creates a direct chat and go to open that chat
// from the all or something like that

//add the socket in get direct list
// get the all user and create a direct chat
//pagination and is read checked something
//update the lazy column etc
//update the screen
//get all use etc and


//fahad
// Add the delete command and go the back screen if it is successful
// list all the chats registered user and on click on any it establishes the condition
// and we got to the chat and open it
//set the navigation in an correct way

//Questions
//what are delete response
// what to read
// issue with the date
