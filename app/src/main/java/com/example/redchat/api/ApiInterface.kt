package com.example.redchat.api

import com.example.redchat.models.LoginRequest
import com.example.redchat.models.LoginResponse
import com.example.redchat.models.SignupRequest
import com.example.redchat.models.SignupResponse
import com.example.redchat.models.UploadImageResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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
    @POST("/api/user/uploadProfilePhoto")
    suspend fun uploadProfileImage(
        @Header("Authorization") bearer_token: String,
        @Part image: MultipartBody.Part
    ): Response<UploadImageResponse>

}