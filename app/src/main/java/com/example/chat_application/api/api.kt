package com.example.chat_application.api

import com.example.chat_application.models.LoginResponse
import com.example.chat_application.models.RegisterResponse
import com.example.chat_application.models.UserItem
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface api{

    @FormUrlEncoded
    @POST("/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
    ) : Response<String>

    @GET("/users")
    suspend fun getUsers(): Response<List<UserItem>>

}