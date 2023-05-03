package com.example.storyappintermediate.api
import com.example.storyappintermediate.model.User

data class RegisterResponse(
    val error: Boolean,
    val message: String
)

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: User
)


data class LoginCredentials(
    val email: String,
    val password: String
)