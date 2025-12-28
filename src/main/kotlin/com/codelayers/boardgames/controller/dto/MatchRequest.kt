package com.codelayers.boardgames.controller.dto

import com.codelayers.boardgames.repository.entity.MatchTiebreaker
import java.time.Instant

data class MatchRequest (
    val players: List<PlayerInfo>,
    val winner: String,
    val tieBreak: MatchTiebreaker,
    val gameType: GameType,
    val playedAt: Instant
)