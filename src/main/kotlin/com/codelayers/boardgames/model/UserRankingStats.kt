package com.codelayers.boardgames.model

import java.math.BigDecimal

data class UserRankingStats(
    val username: String,
    val wins: Int,
    val gamesPlayed: Int,
    val winRate: BigDecimal,
    val lastMatch: MatchResult,
    val currentStreak: Int
) : UserStats
