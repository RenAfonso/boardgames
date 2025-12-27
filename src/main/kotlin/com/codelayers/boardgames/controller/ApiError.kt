package com.codelayers.boardgames.controller

data class ApiError(
    val status: Int,
    val error: String,
    val message: String
)
