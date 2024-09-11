package com.example.redchat.api

import com.example.redchat.models.AcceptFriendRequestResponse
import com.example.redchat.models.LoginRequest
import com.example.redchat.models.LoginResponse
import com.example.redchat.models.SendFriendRequestRequest
import com.example.redchat.models.SendFriendRequestResponse
import com.example.redchat.models.SignupRequest
import com.example.redchat.models.SignupResponse
import com.example.redchat.models.UploadImageResponse
import com.example.redchat.models.loadChat.loadChatResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @POST("/api/auth/logIn")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("/api/auth/signup")
    suspend fun signup(
        @Body signupRequest: SignupRequest
    ): Response<SignupResponse>

    @Multipart
    @PUT("/api/user/uploadProfilePhoto")
    suspend fun uploadProfileImage(
        @Header("Authorization") bearer_token: String,
        @Part image: MultipartBody.Part
    ): Response<UploadImageResponse>

    @POST("/api/user/sendFriendRequest")
    suspend fun sendFriendRequest(
        @Header("Authorization") bearer_token: String,
        @Body username: SendFriendRequestRequest
    ): Response<SendFriendRequestResponse>

    @POST("/api/user/acceptFriendRequest")
    suspend fun acceptFriendRequest(
        @Header("Authorization")    token: String,
        @Body username: SendFriendRequestRequest
    ): Response<AcceptFriendRequestResponse>

    @DELETE("/api/user/rejectFriendRequest/{username}")
    suspend fun rejectFriendRequest(
        @Header("Authorization")    token: String,
        @Path("username") username: String
    ): Response<SendFriendRequestResponse>

    @GET("/api/chat/getMessages")
    suspend fun loadChats(
        @Header("Authorization")    token: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("conversationId") conversationId: String
    ): Response<loadChatResponse>
}