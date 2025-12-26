package com.codelayers.boardgames.model

import com.codelayers.boardgames.repository.entity.GameRankingRow
import java.math.BigDecimal
import java.math.RoundingMode

fun GameRankingRow.toUserRankingStats(lastMatch: MatchResult, currentStreak: Int): UserRankingStats =
    UserRankingStats(
        username = username,
        wins = wins.toInt(),
        gamesPlayed = gamesPlayed.toInt(),
        winRate = if (gamesPlayed > 0)
            BigDecimal(wins)
                .multiply(BigDecimal("100"))
                .divide(BigDecimal(gamesPlayed), 2, RoundingMode.HALF_UP)
        else
            BigDecimal.ZERO,
        lastMatch = lastMatch,
        currentStreak = currentStreak,
    )