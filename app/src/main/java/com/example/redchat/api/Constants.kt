package com.example.redchat.api

import com.example.redchat.models.UserData

val baseUrl = "https://redchat.azurewebsites.net/"
var access_token: String? = null

lateinit var userFromReq: UserData