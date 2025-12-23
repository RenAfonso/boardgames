package com.codelayers.boardgames.controller.model

data class GameRankingResponse(
    val game: String,
    val variants: List<String>,
    val stats: List<UserRankingStats>
)
