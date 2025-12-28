package com.codelayers.boardgames.model

import java.math.BigDecimal

data class GameUserRank(
    val username: String,
    val winRate: BigDecimal,
    val gamesPlayed: Int,
    val gamesWon: Int,
    val averageVP: BigDecimal,
    val currentStreak: Int,
    val lastMatch: MatchResult
) : UserStats
