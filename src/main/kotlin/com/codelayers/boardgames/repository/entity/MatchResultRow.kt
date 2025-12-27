package com.codelayers.boardgames.repository.entity

import java.time.Instant

interface MatchResultRow : HasWinResult {
    override val won: Boolean
    val playedAt: Instant
}