package com.codelayers.boardgames.auth.api

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)
