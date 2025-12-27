package com.codelayers.boardgames.controller.dto

data class PlayerInfo(
    val player: String,
    val playerVP: Int,
    val playerLeader: String? = null,
    val playerTeam: String? = null
)
