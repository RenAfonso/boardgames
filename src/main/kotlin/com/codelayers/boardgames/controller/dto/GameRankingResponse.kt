package com.codelayers.boardgames.controller.dto

import com.codelayers.boardgames.model.UserRankingStats

data class GameRankingResponse(
    val game: String,
    val variants: List<String>,
    val stats: List<UserRankingStats>
)