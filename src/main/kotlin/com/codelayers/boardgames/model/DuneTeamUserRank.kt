package com.codelayers.boardgames.model

import java.math.BigDecimal

data class DuneTeamUserRank(
    val username: String,
    val winRate: BigDecimal,
    val gamesPlayed: Int,
    val gamesWon: Int,
    val winRateFremen: BigDecimal,
    val gamesPlayedFremenPeasant: Int,
    val winRateEmperor: BigDecimal,
    val gamesPlayedEmperorPeasant: Int,
    val winRatePaul: BigDecimal,
    val gamesPlayedPaul: Int,
    val winRateShaddam: BigDecimal,
    val gamesPlayedShaddam: Int,
    val averageVPPeasant: BigDecimal,
    val averageVPLeader: BigDecimal,
    val currentStreak: Int,
    val lastMatch: MatchResult
) : UserStats
