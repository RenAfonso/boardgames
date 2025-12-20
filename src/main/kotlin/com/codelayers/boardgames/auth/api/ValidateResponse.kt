package com.codelayers.boardgames.auth.api

data class ValidateResponse(
    val userId: String,
    val username: String,
    val roles: List<String>
)
