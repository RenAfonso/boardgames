package com.codelayers.boardgames.repository.entity

import java.time.Instant
import java.util.UUID

interface GameRankingRow {
    val userId: UUID
    val username: String
    val gamesPlayed: Long
    val wins: Long
    val lastPlayedAt: Instant
}