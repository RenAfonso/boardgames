package com.codelayers.boardgames.controller.dto

import com.codelayers.boardgames.repository.entity.MatchTiebreaker

data class MatchRequest (
    val players: List<PlayerInfo>,
    val winner: String,
    val tieBreak: MatchTiebreaker,
    val gameType: GameType
)