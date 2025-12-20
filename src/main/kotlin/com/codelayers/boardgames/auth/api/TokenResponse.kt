package com.codelayers.boardgames.auth.api

data class TokenResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresInMinutes: Long
)
