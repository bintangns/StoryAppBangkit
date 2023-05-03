package com.example.storyappintermediate.api
import com.example.storyappintermediate.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface StoryApi {
    @POST("v1/register")
    fun register(@Body user: User): Call<RegisterResponse>

    @POST("v1/login")
    fun login(@Body credentials: LoginCredentials): Call<LoginResponse>
}