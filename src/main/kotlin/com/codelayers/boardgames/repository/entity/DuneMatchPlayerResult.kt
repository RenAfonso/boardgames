package com.codelayers.boardgames.repository.entity

import java.time.Instant
import java.util.UUID

interface DuneMatchPlayerResult : HasWinResult {
    val userId: UUID
    val username: String
    val points: Int
    override val won: Boolean
    val teamSide: String?
    val matchPlayerId: UUID
    val playedAt: Instant
    val leaderName: String
}
