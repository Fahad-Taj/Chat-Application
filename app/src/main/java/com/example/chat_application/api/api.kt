package com.example.chat_application.api

import com.example.chat_application.models.CreateDirectChat.CreateDirectChatRequest
import com.example.chat_application.models.CreateDirectChat.CreateDirectChatResponse
import com.example.chat_application.models.DirectChatsResponse
import com.example.chat_application.models.GetAllUsers.GetAllUsers
import com.example.chat_application.models.LoginResponse
import com.example.chat_application.models.Messages
import com.example.chat_application.models.RegisterResponse
import com.example.chat_application.models.UserItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface api {

    @FormUrlEncoded
    @POST("/login")
    suspend fun login(
        @Field("username") username: String, @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
    ): Response<String>

    @GET("/users")
    suspend fun getUsers(): Response<List<UserItem>>

    @GET("chat/{chat_guid}/messages/")
    suspend fun getMessages(
        @Path("chat_guid") chatGuid: String,
        @Query("size") size: Int,
    ): Response<Messages>

    @GET("/chats/direct")
    suspend fun getDirectChats(): Response<DirectChatsResponse>

    @GET("/users/")
    suspend fun getAllUsers(): Response<GetAllUsers>

    @POST("/chat/direct")
    suspend fun createDirectChat(@Body recipient_user_guid: CreateDirectChatRequest)
            : Response<CreateDirectChatResponse>
}