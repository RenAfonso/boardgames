package com.codelayers.boardgames.auth.api

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)
