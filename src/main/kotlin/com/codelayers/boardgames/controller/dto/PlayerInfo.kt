package com.codelayers.boardgames.controller.dto

data class PlayerInfo(
    val username: String,
    val vp: Int,
    val startingPosition: Int,
    val leader: String? = null,
    val team: String? = null
)
